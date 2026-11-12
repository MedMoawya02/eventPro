package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.RefundRequest;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefundServiceTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private PaiementRepository paiementRepository;
    @Mock private RemboursementRepository remboursementRepository;
    @Mock private BilletRepository billetRepository;
    @Mock private CategorieBilletRepository categorieBilletRepository;

    @InjectMocks
    private RefundService refundService;

    @Test
    void testRemboursementPlusDe48h() {
        // Arrange : Événement dans 10 jours
        Evenement event = Evenement.builder()
                .dateDebut(LocalDateTime.now().plusDays(10))
                .build();

        CategorieBillet cat = CategorieBillet.builder()
                .evenement(event)
                .placesVendues(2)
                .build();

        LigneReservation ligne = LigneReservation.builder()
                .categorieBillet(cat)
                .quantite(2)
                .build();

        Reservation reservation = Reservation.builder()
                .id(1L)
                .reference("RES-123")
                .status(StatutReservation.CONFIRMED)
                .lignes(List.of(ligne))
                .build();

        Paiement paiement = Paiement.builder()
                .montant(new BigDecimal("1000"))
                .build();

        RefundRequest request = new RefundRequest();
        request.setReservationReference("RES-123");

        when(reservationRepository.findByReference("RES-123")).thenReturn(Optional.of(reservation));
        when(paiementRepository.findByReservationId(1L)).thenReturn(Optional.of(paiement));
        when(billetRepository.findByLigneReservationReservationId(1L)).thenReturn(List.of());
        when(remboursementRepository.save(any(Remboursement.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Remboursement result = refundService.demanderRemboursement(request);

        // Assert
        assertEquals(StatutRemboursement.APPROVED, result.getStatut());
        assertEquals(new BigDecimal("1000"), result.getMontant());
        assertEquals(StatutReservation.REFUNDED, reservation.getStatus());
    }

    @Test
    void testRemboursementMoinsDe48h() {
        // Arrange : Événement dans 24h
        Evenement event = Evenement.builder()
                .dateDebut(LocalDateTime.now().plusHours(24))
                .build();

        CategorieBillet cat = CategorieBillet.builder()
                .evenement(event)
                .build();

        LigneReservation ligne = LigneReservation.builder()
                .categorieBillet(cat)
                .quantite(2)
                .build();

        Reservation reservation = Reservation.builder()
                .id(1L)
                .reference("RES-123")
                .status(StatutReservation.CONFIRMED)
                .lignes(List.of(ligne))
                .build();

        Paiement paiement = Paiement.builder()
                .montant(new BigDecimal("1000"))
                .build();

        RefundRequest request = new RefundRequest();
        request.setReservationReference("RES-123");

        when(reservationRepository.findByReference("RES-123")).thenReturn(Optional.of(reservation));
        when(paiementRepository.findByReservationId(1L)).thenReturn(Optional.of(paiement));
        when(remboursementRepository.save(any(Remboursement.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Remboursement result = refundService.demanderRemboursement(request);

        // Assert
        assertEquals(StatutRemboursement.REJECTED, result.getStatut());
        assertEquals(BigDecimal.ZERO, result.getMontant());
        assertEquals(StatutReservation.CONFIRMED, reservation.getStatus());
    }
}