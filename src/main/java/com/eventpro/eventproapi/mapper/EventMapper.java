package com.eventpro.eventproapi.mapper;

import com.eventpro.eventproapi.dto.response.EventResponse;
import com.eventpro.eventproapi.entity.CategorieBillet;
import com.eventpro.eventproapi.entity.Evenement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    // 1. Mapping principal (Evenement -> EventResponse)
    @Mapping(source = "titre", target = "title")
    @Mapping(source = "lieu", target = "location")
    @Mapping(source = "dateDebut", target = "startDate")
    @Mapping(source = "dateFin", target = "endDate")
    @Mapping(source = "statut", target = "status")
    @Mapping(source = "organisateur.organisationName", target = "organizerName")
    @Mapping(source = "categories", target = "categories")
    EventResponse toResponse(Evenement evenement);

    // 2. Mapping pour les catégories (CategorieBillet -> CategoryResponse)
    @Mapping(source = "category.name", target = "categoryName") // Récupère le nom depuis TicketCategory
    @Mapping(source = "prixUnitaire", target = "unitPrice")
    @Mapping(source = "quantiteTotale", target = "totalQuantity")
    @Mapping(source = "quotaMaxParClient", target = "maxQuotaPerClient")
    @Mapping(target = "availablePlaces", expression = "java(c.getPlacesDisponibles())") // Appel de la méthode de l'entité
    EventResponse.CategoryResponse toCategoryResponse(CategorieBillet c);
}