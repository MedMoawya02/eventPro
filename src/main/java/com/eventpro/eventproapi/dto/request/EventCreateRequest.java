package com.eventpro.eventproapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventCreateRequest {

    @NotNull(message = "L'ID de l'organisateur est requis")
    private Long organizerId;

    @NotBlank(message = "Le titre est requis")
    private String title;

    private String description;

    @NotBlank(message = "Le lieu est requis")
    private String location;

    @NotNull(message = "La date de début est requise")
    @Future(message = "La date de début doit être dans le futur")
    private LocalDateTime startDate;

    @NotNull(message = "La date de fin est requise")
    @Future(message = "La date de fin doit être dans le futur")
    private LocalDateTime endDate;

    @NotNull(message = "La date d'ouverture des ventes est requise")
    private LocalDateTime salesOpeningDate;

    @NotNull(message = "La date de clôture des ventes est requise")
    private LocalDateTime salesClosingDate;

    @Min(value = 1, message = "La capacité doit être d'au moins 1")
    private int capacity;

    @NotEmpty(message = "L'événement doit avoir au moins une catégorie")
    @Valid
    private List<CategoryRequest> categories;

    @Data
    public static class CategoryRequest {

        @NotNull(message = "L'ID de la catégorie est requis")
        private Long categoryId;

        @NotNull(message = "Le prix est requis")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
        private BigDecimal unitPrice;

        @Min(value = 1, message = "La quantité totale doit être d'au moins 1")
        private int totalQuantity;

        @Min(value = 1, message = "Le quota par client doit être d'au moins 1")
        private int maxQuotaPerClient;
    }
}