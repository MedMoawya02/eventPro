package com.eventpro.eventproapi.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservationResponse {

    private String reference;
    private LocalDateTime dateCreation;
    private LocalDateTime dateExpiration;
    private String statut;
    private BigDecimal sousTotal;
    private BigDecimal remise;
    private BigDecimal montantTotal;

    // La liste des lignes de la réservation
    private List<LigneResponse> lignes;

    @Data
    @Builder
    public static class LigneResponse {
        private String categorieName;
        private int quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal sousTotal;
    }
}