package com.eventpro.eventproapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotBlank(message = "La clé d'idempotence est requis")
    private String idempotencyKey;
    @NotBlank(message = "la reference de la réservation est requis")
    private String reservationReference;
    @NotBlank(message = "le montant est requis")
    private BigDecimal montant;
}
