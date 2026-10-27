package com.eventpro.eventproapi.repository;

import com.eventpro.eventproapi.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement,Long> {
    Optional<Paiement> findByIdempotencyKey(String idempotencyKey);
    // Optionnel : pour retrouver le paiement lié à une réservation
    Optional<Paiement> findByReservationId(Long reservationId);
}
