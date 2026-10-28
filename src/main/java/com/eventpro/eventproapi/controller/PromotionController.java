package com.eventpro.eventproapi.controller;

import com.eventpro.eventproapi.dto.request.PaymentRequest;
import com.eventpro.eventproapi.dto.request.PromoRequest;
import com.eventpro.eventproapi.entity.Promotion;
import com.eventpro.eventproapi.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Promotion creerPromotion(@Valid @RequestBody PromoRequest request){
        return promotionService.creerPromotion(request);
    }

    @GetMapping
    public List<Promotion> getAllPromotions(){
        return  promotionService.getAllPromotions();
    }

}
