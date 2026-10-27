package com.eventpro.eventproapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReservationRequest {
    @NotNull(message = "L'ID du client est requis")
    private Long clientId;
    private String promoCode;

    @NotEmpty(message = "La réservation doit contenir au moins une ligne")
    private List<LigneRequest> lignes;

    @Data
    public static class LigneRequest{
        @NotNull(message = "L'ID de la catégorie est requis")
        private Long categorieId;

        @Min(value = 1, message = "La quantité doit être d'au moins 1")
        private int quantite;

    }
}
