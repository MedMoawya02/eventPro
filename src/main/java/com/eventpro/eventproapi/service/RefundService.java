package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.RefundRequest;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;
    private final BilletRepository billetRepository;
    private final CategorieBilletRepository categorieBilletRepository;
    private final RemboursementRepository remboursementRepository;

    @Transactional
    public Remboursement demanderRemboursement(RefundRequest request){
        Reservation reservation=reservationRepository.findByReference(request.getReservationReference()).orElseThrow(()->new ResourceNotFoundException("Réservation introuvable"));
        if(!reservation.getStatus().equals(StatutReservation.CONFIRMED)){
            throw new IllegalStateException("seule une reservation payée peut etre remboursee");
        }
        // verification que n'a pas deja un remboursement
        if(reservation.getStatus()==StatutReservation.REFUNDED){
            throw new IllegalStateException("Cette réservation a déja été remboursé");
        }
        //Récupération de l'even
        Evenement evenement=reservation.getLignes().get(0).getCategorieBillet().getEvenement();
        LocalDateTime now=LocalDateTime.now();
        Duration délai= Duration.between(now, evenement.getDateDebut());
        long heuresRestentes=délai.toHours();
        //Récupération du paiement associé
        Paiement paiement=paiementRepository.findByReservationId(reservation.getId()).orElseThrow(()->new ResourceNotFoundException("Paiement introuvable"));
        // création du remboursement
        Remboursement remboursement=Remboursement.builder()
                .paiement(paiement)
                .dateDemande(LocalDateTime.now())
                .motif(request.getMotif())
                .build();
        if(heuresRestentes>=48){
            remboursement.setMontant(paiement.getMontant());
            remboursement.setStatut(StatutRemboursement.APPROVED);
            // Invalider les billets
            for(LigneReservation ligne: reservation.getLignes()){
                CategorieBillet cat=ligne.getCategorieBillet();
                // Rendre les places disponibles
                cat.setPlacesVendues(cat.getPlacesVendues()- ligne.getQuantite());
                categorieBilletRepository.save(cat);
            }
            //passer les billets en cancelled
            List<Billet> billets=billetRepository.findByLigneReservationReservationId(reservation.getId());
            for(Billet b:billets){
                b.setStatut(StatutBillet.CANCELLED);
                billetRepository.save(b);
            }
            //passer la reservation en refund
            reservation.setStatus(StatutReservation.REFUNDED);
            reservationRepository.save(reservation);
        }else {
            //Refus
            remboursement.setMontant(BigDecimal.ZERO);
            remboursement.setStatut(StatutRemboursement.REJECTED);
        }
        return remboursementRepository.save(remboursement);
    }
}
