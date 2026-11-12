package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.EventCreateRequest;
import com.eventpro.eventproapi.dto.response.EventResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.mapper.EventMapper;
import com.eventpro.eventproapi.repository.EvenementRepository;
import com.eventpro.eventproapi.repository.TicketCategoryRepository;
import com.eventpro.eventproapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock private EvenementRepository evenementRepository;
    @Mock private UserRepository userRepository;
    @Mock private EventMapper eventMapper;
    @Mock private TicketCategoryRepository ticketCategoryRepository;
    @InjectMocks
    private EventService eventService;

    @Test
    void testCreerEvenementReussi() {
        // Arrange
        Organizer orga = new Organizer();
        orga.setId(1L);
        orga.setName("Bob");

        TicketCategory category = new TicketCategory();
        category.setId(1L);
        category.setName("VIP");

        EventCreateRequest.CategoryRequest catReq = new EventCreateRequest.CategoryRequest();
        catReq.setCategoryId(1L);
        catReq.setUnitPrice(new BigDecimal("500"));
        catReq.setTotalQuantity(100);
        catReq.setMaxQuotaPerClient(4);

        EventCreateRequest request = new EventCreateRequest();
        request.setOrganizerId(1L);
        request.setTitle("Concert");
        request.setCategories(List.of(catReq));

        when(userRepository.findById(1L)).thenReturn(Optional.of(orga));
        when(ticketCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(evenementRepository.save(any(Evenement.class))).thenAnswer(inv -> inv.getArgument(0));
        when(eventMapper.toResponse(any(Evenement.class))).thenReturn(EventResponse.builder().build());

        // Act
        EventResponse response = eventService.creerEvenement(request);

        // Assert
        assertNotNull(response);
        verify(evenementRepository).save(any(Evenement.class));
    }

    @Test
    void testCreerEvenementOrganisateurIntrouvable() {
        EventCreateRequest request = new EventCreateRequest();
        request.setOrganizerId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.creerEvenement(request);
        });
    }

    @Test
    void testPublierEvenementReussi() {
        Evenement event = Evenement.builder()
                .id(1L)
                .statut(StatutEvenement.DRAFT)
                .build();

        when(evenementRepository.findById(1L)).thenReturn(Optional.of(event));
        when(evenementRepository.save(any(Evenement.class))).thenAnswer(inv -> inv.getArgument(0));
        when(eventMapper.toResponse(any(Evenement.class))).thenReturn(EventResponse.builder().build());

        eventService.publierEvenement(1L);

        assertEquals(StatutEvenement.PUBLISHED, event.getStatut());
    }
}