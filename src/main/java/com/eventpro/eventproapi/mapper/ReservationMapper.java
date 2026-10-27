package com.eventpro.eventproapi.mapper;

import com.eventpro.eventproapi.dto.response.ReservationResponse;
import com.eventpro.eventproapi.entity.LigneReservation;
import com.eventpro.eventproapi.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservationMapper {

    @Mapping(source = "reference", target = "reference")
    @Mapping(source = "creationDate", target = "dateCreation")
    @Mapping(source = "expirationDate", target = "dateExpiration")
    @Mapping(source = "status", target = "statut")
    @Mapping(source = "lignes", target = "lignes") // On utilise "lines" car c'est ce que MapStruct a trouvé
    ReservationResponse toResponse(Reservation reservation);

    // On utilise LigneReservation partout
    @Mapping(target = "categorieName", expression = "java(ligne.getCategorieBillet() != null && ligne.getCategorieBillet().getCategory() != null ? ligne.getCategorieBillet().getCategory().getName() : \"Inconnue\")")
    @Mapping(source = "quantite", target = "quantite")
    @Mapping(source = "prixUnitaire", target = "prixUnitaire")
    @Mapping(source = "sousTotal", target = "sousTotal")
    ReservationResponse.LigneResponse toLigneResponse(LigneReservation ligne);
}