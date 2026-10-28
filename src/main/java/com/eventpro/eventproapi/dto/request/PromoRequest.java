package com.eventpro.eventproapi.dto.request;

import com.eventpro.eventproapi.entity.TypeReduction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PromoRequest {
    @NotBlank(message = "Le code est requis")
    private String code;
    @NotNull(message = "Le type de réduction")
    private TypeReduction typeReduction;
    @NotNull(message="La valeur de la réduction est requise")
    @DecimalMin(value="0.0",inclusive = false)
    private BigDecimal valeurReduction;
    @NotNull(message = "La date de debut est requise")
    private LocalDateTime dateDebut;
    @NotNull(message = "la date de fin est requise")
    private LocalDateTime dateFin;
    @Min(value = 1, message = "La limite d'utilisation doit être d'au moins 1")
    private int limiteUtilisation;
}
