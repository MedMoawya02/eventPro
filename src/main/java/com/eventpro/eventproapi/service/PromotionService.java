package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.PromoRequest;
import com.eventpro.eventproapi.entity.Promotion;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    @Transactional
    public Promotion creerPromotion(PromoRequest request){
        if(promotionRepository.findByCode(request.getCode()).isPresent()){
            throw new IllegalArgumentException("Ce code promo existe deja.");
        }
        Promotion promotion=Promotion.builder()
                .code(request.getCode())
                .typeReduction(request.getTypeReduction())
                .valeurReduction(request.getValeurReduction())
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .limiteUtilisation(request.getLimiteUtilisation())
                .nombreUtilisations(0)
                .build();
        return promotionRepository.save(promotion);
    }

    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }
    public Promotion validerCode(String code){
        Promotion promo=promotionRepository.findByCode(code).orElseThrow(()->new ResourceNotFoundException("code promo invalide"));
        if(!promo.estValide()){
            throw new IllegalArgumentException("Ce code promo a expiré");
        }
        return promo;
    }
}
