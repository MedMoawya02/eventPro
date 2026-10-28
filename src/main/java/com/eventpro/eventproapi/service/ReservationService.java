package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.ReservationRequest;
import com.eventpro.eventproapi.dto.response.ReservationResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.InsufficientStockException;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.mapper.ReservationMapper;
import com.eventpro.eventproapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CategorieBilletRepository categorieBillet;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;
    private final PromotionRepository promotionRepository;
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request){
        Client client=(Client)userRepository.findById(request.getClientId()).orElseThrow(()->new ResourceNotFoundException("Utilisateur introuvable"));
        // 2. Vérifier la capacité globale de l'événement
        // On récupère la première catégorie pour connaître l'événement
        CategorieBillet premiereCategorie=categorieBillet.findById(request.getLignes().get(0).getCategorieId()).orElseThrow(()->new ResourceNotFoundException("Catégorie introuvable"));
        Evenement evenement=premiereCategorie.getEvenement();
        int placesDejaPrises=reservationRepository.countPlacesReserveesPourEvenement(evenement.getId());
        int placesDemandes=request.getLignes().stream().mapToInt(ReservationRequest.LigneRequest::getQuantite).sum();
        if(placesDejaPrises+placesDemandes> evenement.getCapacite()){
            throw new InsufficientStockException(
                    "La capacité maximale de l'événement est atteinte. Places restantes : "
                            + (evenement.getCapacite() - placesDejaPrises)
            );
        }
        // 3. Créer la réservation
        Reservation reservation=Reservation.builder()
                .reference("RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .client(client)
                .creationDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusMinutes(10))
                .status(StatutReservation.PENDING_PAYMENT)
                .lignes(new ArrayList<>())
                .build();
        BigDecimal total=BigDecimal.ZERO;
        for(ReservationRequest.LigneRequest ligneRequest:request.getLignes()){
            CategorieBillet categorie=categorieBillet.findByIdWithLock(ligneRequest.getCategorieId()).orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));
            if(!categorie.verifierStock(ligneRequest.getQuantite())){
                throw new InsufficientStockException("Plus assez de places");
            }
            // 4.b Vérifier le quota max par client
            int dejaAchete=reservationRepository.countPlacesAcheteesParClientEtCategorie(client.getId(), categorie.getId());
            if(dejaAchete+ligneRequest.getQuantite()> categorie.getQuotaMaxParClient()){
                throw new IllegalStateException(
                        "Vous avez atteint votre quota maximum de " + categorie.getQuotaMaxParClient()
                                + " places pour la catégorie "
                );
            }
            //
            categorie.setPlacesReservees(categorie.getPlacesReservees()+ligneRequest.getQuantite());
            categorieBillet.save(categorie);
            LigneReservation ligne=LigneReservation.builder()
                    .reservation(reservation)
                    .categorieBillet(categorie)
                    .quantite(ligneRequest.getQuantite())
                    .prixUnitaire(categorie.getPrixUnitaire())
                    .sousTotal(categorie.getPrixUnitaire().multiply(BigDecimal.valueOf(ligneRequest.getQuantite())))
                    .build();
                    reservation.getLignes().add(ligne);
                    total=total.add(ligne.getSousTotal());
        }
        //5.appliquer le code promo
        if(request.getPromoCode()!=null&&!request.getPromoCode().isEmpty()){
            Promotion promotion=promotionRepository.findByCode(request.getPromoCode()).orElseThrow(()->new ResourceNotFoundException("code promo invalid"));
            if(!promotion.estValide()){
                throw new IllegalStateException("Ce code promo a expiré ou a atteint sa limite.");
            }
            BigDecimal remise=promotion.calculerReduction(total);
            total=total.subtract(remise).max(BigDecimal.ZERO);
            reservation.setPromotion(promotion);
            promotion.setNombreUtilisations(promotion.getNombreUtilisations()+1);
            promotionRepository.save(promotion);
        }
        //
        reservation.setTotalAmount(total);
        Reservation saved=reservationRepository.save(reservation);
        return reservationMapper.toResponse(saved);
//        reservationRepository.save(reservation);
    }
}
