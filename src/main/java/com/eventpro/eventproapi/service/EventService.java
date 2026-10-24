package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.EventCreateRequest;
import com.eventpro.eventproapi.dto.response.EventResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.repository.EvenementRepository;
import com.eventpro.eventproapi.repository.TicketCategoryRepository;
import com.eventpro.eventproapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EvenementRepository evenementRepository;
    // On retire TicketCategoryRepository car nous n'en avons plus besoin
    private final UserRepository userRepository;
    private final TicketCategoryRepository ticketCategoryRepository;

    @Transactional
    public EventResponse creerEvenement(EventCreateRequest request) {

        // 1. Vérifier l'organisateur
        User user = userRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Organisateur introuvable"));

        if (!(user instanceof Organizer)) {
            throw new IllegalArgumentException("L'utilisateur spécifié n'est pas un organisateur.");
        }
        Organizer organizer = (Organizer) user;

        // 2. Créer l'événement
        Evenement evenement = Evenement.builder()
                .organisateur(organizer)
                .titre(request.getTitle())
                .description(request.getDescription())
                .lieu(request.getLocation())
                .dateDebut(request.getStartDate())
                .dateFin(request.getEndDate())
                .dateOuvertureVente(request.getSalesOpeningDate())
                .dateClotureVente(request.getSalesClosingDate())
                .statut(StatutEvenement.DRAFT)
                .categories(new ArrayList<>())
                .build();

        // 3. Ajouter les catégories
        for (EventCreateRequest.CategoryRequest catReq : request.getCategories()) {
            // on cherche lecatégorie
            TicketCategory ticketCategory=ticketCategoryRepository.findById(catReq.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + catReq.getCategoryId()));
            CategorieBillet categorie = CategorieBillet.builder()
                    .evenement(evenement)
                    .category(ticketCategory)
                    .prixUnitaire(catReq.getUnitPrice())
                    .quantiteTotale(catReq.getTotalQuantity())
                    .quotaMaxParClient(catReq.getMaxQuotaPerClient())
                    .placesReservees(0)
                    .placesVendues(0)
                    .build();

            evenement.getCategories().add(categorie);
        }

        // 4. Sauvegarder en cascade
        Evenement saved = evenementRepository.save(evenement);

        // 5. Retourner la réponse
        return mapToResponse(saved);
    }
    @Transactional
    public EventResponse publierEvenement(Long eventID){
        //1.cherchez l'event
        Evenement evenement=evenementRepository.findById(eventID).orElseThrow(()->new ResourceNotFoundException("Evenement introuvable avec l'id : "+eventID));
        if(evenement.getStatut()!=StatutEvenement.DRAFT){
            throw new IllegalArgumentException("Seul un évenement en brouillon (DRAFT) peut étre publié.Statut actuel : "+evenement.getStatut());
        }
        //changer le statut
        evenement.publier();
        //sauvegarder
        Evenement saved=evenementRepository.save(evenement);
        //retourner la reponse
        return mapToResponse(saved);

    }
    private EventResponse mapToResponse(Evenement event) {
        List<EventResponse.CategoryResponse> categories = event.getCategories().stream()
                .map(c -> EventResponse.CategoryResponse.builder()
                        .id(c.getId())
                        .categoryName(c.getCategory().getName())
                        .unitPrice(c.getPrixUnitaire())
                        .totalQuantity(c.getQuantiteTotale())
                        .availablePlaces(c.getPlacesDisponibles())
                        .maxQuotaPerClient(c.getQuotaMaxParClient())
                        .build())
                .toList();

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitre())
                .description(event.getDescription())
                .location(event.getLieu())
                .startDate(event.getDateDebut())
                .endDate(event.getDateFin())
                .status(event.getStatut().name())
                .organizerName(event.getOrganisateur().getOrganisationName())
                .categories(categories)
                .build();
    }
}