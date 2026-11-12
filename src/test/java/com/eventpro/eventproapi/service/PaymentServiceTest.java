package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.PaymentRequest;
import com.eventpro.eventproapi.dto.response.PaymentResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private PaiementRepository paiementRepository;
    @Mock private ReservationRepository reservationRepository;
    @Mock private BilletRepository billetRepository;
    @Mock private CategorieBilletRepository categorieBilletRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void testPayerReussi() {
        // Arrange
        Reservation reservation = Reservation.builder()
                .id(1L)
                .reference("RES-123")
                .status(StatutReservation.PENDING_PAYMENT)
                .expirationDate(LocalDateTime.now().plusMinutes(5))
                .totalAmount(new BigDecimal("1000"))
                .lignes(new ArrayList<>())
                .build();

        PaymentRequest request = new PaymentRequest();
        request.setIdempotencyKey("KEY-123");
        request.setReservationReference("RES-123");
        request.setMontant(new BigDecimal("1000"));

        when(paiementRepository.findByIdempotencyKey("KEY-123")).thenReturn(Optional.empty());
        when(reservationRepository.findByReference("RES-123")).thenReturn(Optional.of(reservation));
        when(paiementRepository.save(any(Paiement.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        PaymentResponse response = paymentService.payer(request);

        // Assert
        assertNotNull(response);
        assertEquals(StatutReservation.CONFIRMED, reservation.getStatus());
        verify(paiementRepository).save(any(Paiement.class));
    }

    @Test
    void testPayerIdempotenceEchouee() {
        Paiement existing = new Paiement();
        when(paiementRepository.findByIdempotencyKey("KEY-123")).thenReturn(Optional.of(existing));

        PaymentRequest request = new PaymentRequest();
        request.setIdempotencyKey("KEY-123");

        assertThrows(IllegalStateException.class, () -> {
            paymentService.payer(request);
        });
    }

    @Test
    void testPayerReservationExpiree() {
        Reservation reservation = Reservation.builder()
                .reference("RES-123")
                .status(StatutReservation.PENDING_PAYMENT)
                .expirationDate(LocalDateTime.now().minusMinutes(1)) // Expirée
                .build();

        PaymentRequest request = new PaymentRequest();
        request.setIdempotencyKey("KEY-123");
        request.setReservationReference("RES-123");

        when(paiementRepository.findByIdempotencyKey("KEY-123")).thenReturn(Optional.empty());
        when(reservationRepository.findByReference("RES-123")).thenReturn(Optional.of(reservation));

        assertThrows(IllegalStateException.class, () -> {
            paymentService.payer(request);
        });
    }
}