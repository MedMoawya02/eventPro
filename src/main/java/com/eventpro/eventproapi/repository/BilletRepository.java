package com.eventpro.eventproapi.repository;

import com.eventpro.eventproapi.entity.Billet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BilletRepository extends JpaRepository<Billet,Long> {
    Optional<Billet> findByCode(String id);
    List<Billet> findByLigneReservationReservationId(Long reservationId);
}
