package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.ReservationRequest;
import com.eventpro.eventproapi.dto.response.ReservationResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.InsufficientStockException;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.mapper.ReservationMapper;
import com.eventpro.eventproapi.repository.CategorieBilletRepository;
import com.eventpro.eventproapi.repository.ReservationRepository;
import com.eventpro.eventproapi.repository.TicketCategoriesPerEventRepository;
import com.eventpro.eventproapi.repository.UserRepository;
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
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request){
        Client client=(Client)userRepository.findById(request.getClientId()).orElseThrow(()->new ResourceNotFoundException("Utilisateur introuvable"));

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
        reservation.setTotalAmount(total);
        Reservation saved=reservationRepository.save(reservation);
        return reservationMapper.toResponse(saved);
//        reservationRepository.save(reservation);
    }
}
