package com.eventpro.eventproapi.repository;

import com.eventpro.eventproapi.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Pour retrouver une réservation par sa référence
    Optional<Reservation> findByReference(String reference);

    // Pour la suppression d'un client (s'il a des réservations)
    boolean existsByClientId(Long id);

    // Compter toutes les places prises pour un événement (capacité globale)
    @Query("SELECT COALESCE(SUM(l.quantite), 0) FROM LigneReservation l JOIN l.reservation r WHERE r.status IN ('PENDING_PAYMENT', 'CONFIRMED') AND l.categorieBillet.evenement.id = :evenementId")
    Integer countPlacesReserveesPourEvenement(@Param("evenementId") Long evenementId);

    // Compter les places achetées par un client pour une catégorie donnée (quota par client)
    @Query("SELECT COALESCE(SUM(l.quantite), 0) FROM LigneReservation l JOIN l.reservation r WHERE r.client.id = :clientId AND l.categorieBillet.id = :categorieId AND r.status IN ('PENDING_PAYMENT', 'CONFIRMED')")
    Integer countPlacesAcheteesParClientEtCategorie(@Param("clientId") Long clientId, @Param("categorieId") Long categorieId);
}