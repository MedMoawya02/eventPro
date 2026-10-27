package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.PaymentRequest;
import com.eventpro.eventproapi.dto.response.PaymentResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.repository.BilletRepository;
import com.eventpro.eventproapi.repository.CategorieBilletRepository;
import com.eventpro.eventproapi.repository.PaiementRepository;
import com.eventpro.eventproapi.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaiementRepository paiementRepository;
    private final ReservationRepository reservationRepository;
    private final BilletRepository billetRepository;
    private final CategorieBilletRepository categorieBilletRepository;

    @Transactional
    public PaymentResponse payer(PaymentRequest request) {

        // 1. Vérifier l'idempotence
        if (paiementRepository.findByIdempotencyKey(request.getIdempotencyKey()).isPresent()) {
            throw new IllegalStateException("Ce paiement a déjà été traité.");
        }

        // 2. Récupérer la réservation
        Reservation reservation = reservationRepository.findByReference(request.getReservationReference())
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));

        // 3. Vérifier le statut et l'expiration
        if (reservation.getStatus() != StatutReservation.PENDING_PAYMENT) {
            throw new IllegalStateException("La réservation n'est pas en attente de paiement.");
        }
        if (LocalDateTime.now().isAfter(reservation.getExpirationDate())) {
            throw new IllegalStateException("La réservation a expiré.");
        }

        // 4. Créer le paiement
        Paiement paiement = Paiement.builder()
                .reservation(reservation)
                .idempotencyKey(request.getIdempotencyKey())
                .reference("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .montant(request.getMontant())
                .datePaiement(LocalDateTime.now())
                .statut(StatutPaiement.SUCCESS)
                .build();
        paiementRepository.save(paiement);

        // 5. Confirmer la réservation
        reservation.confirm();
        reservationRepository.save(reservation);

        // 6. Transférer le stock et générer les billets
        for (LigneReservation ligne : reservation.getLignes()) {
            CategorieBillet categorie = ligne.getCategorieBillet();

            categorie.setPlacesReservees(categorie.getPlacesReservees() - ligne.getQuantite());
            categorie.setPlacesVendues(categorie.getPlacesVendues() + ligne.getQuantite());
            categorieBilletRepository.save(categorie);

            for (int i = 0; i < ligne.getQuantite(); i++) {
                Billet billet = Billet.builder()
                        .code("TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                        .ligneReservation(ligne)
                        .dateEmission(LocalDateTime.now())
                        .statut(StatutBillet.VALID)
                        .build();
                billetRepository.save(billet);
            }
        }

        // 7. Retourner la réponse
        return PaymentResponse.builder()
                .reference(paiement.getReference())
                .montant(paiement.getMontant())
                .statut(paiement.getStatut().name())
                .datePaiement(paiement.getDatePaiement())
                .build();
    }
}