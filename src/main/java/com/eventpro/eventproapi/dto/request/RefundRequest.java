package com.eventpro.eventproapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class RefundRequest {
    @NotBlank(message = "La référence de la réservation est requise")
    private String reservationReference;

    private String motif;
}
