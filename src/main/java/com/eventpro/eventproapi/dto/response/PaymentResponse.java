package com.eventpro.eventproapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class PaymentResponse {
    private String reference;
    private BigDecimal montant;
    private String statut;
    private LocalDateTime datePaiement;
}
