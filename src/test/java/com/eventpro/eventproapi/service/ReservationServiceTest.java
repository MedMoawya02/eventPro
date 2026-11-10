package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.ReservationRequest;
import com.eventpro.eventproapi.entity.CategorieBillet;
import com.eventpro.eventproapi.entity.Client;
import com.eventpro.eventproapi.entity.Evenement;
import com.eventpro.eventproapi.mapper.ReservationMapper;
import com.eventpro.eventproapi.repository.CategorieBilletRepository;
import com.eventpro.eventproapi.repository.PromotionRepository;
import com.eventpro.eventproapi.repository.ReservationRepository;
import com.eventpro.eventproapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock private ReservationRepository reservationRepository;
    @Mock private CategorieBilletRepository categorieBilletRepository;
    @Mock private UserRepository userRepository;
    @Mock private PromotionRepository promotionRepository;
    @Mock private ReservationMapper reservationMapper;
    @InjectMocks
    private  ReservationService reservationService;
    //helpers
    private Client creerClient(Long id){
        Client c=new Client();
        c.setId(id);
        c.setName("Mohamed");
        return c;
    }

    private Evenement crereEvent(Long id,int capacité){
        return Evenement.builder()
                .id(id).titre("event1").capacite(capacité).dateDebut(LocalDateTime.now().plusDays(30)).build();

    }

    private CategorieBillet creerCategorie(Long id, BigDecimal prix, int stock, int quota, Evenement event) {
        return CategorieBillet.builder()
                .id(id)
                .prixUnitaire(prix)
                .quantiteTotale(stock)
                .placesReservees(0)
                .placesVendues(0)
                .quotaMaxParClient(quota)
                .evenement(event)
                .build();
    }

    private ReservationRequest creerRequest(Long clientId, Long categorieId, int quantite) {
        ReservationRequest request = new ReservationRequest();
        request.setClientId(clientId);
        ReservationRequest.LigneRequest ligne = new ReservationRequest.LigneRequest();
        ligne.setCategorieId(categorieId);
        ligne.setQuantite(quantite);
        request.setLignes(List.of(ligne));
        return request;
    }

    //tests
    @Test
    void  testReservationReussieSansPromo(){
        Client client=creerClient(1L);
        Evenement evenement=crereEvent(1L,100);
        CategorieBillet categorie=creerCategorie(1L,new BigDecimal("500"),50,4,evenement);
        ReservationRequest request=creerRequest(1L,1L,2);

    }
}
