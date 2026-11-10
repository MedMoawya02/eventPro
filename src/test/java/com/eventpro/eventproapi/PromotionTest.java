package com.eventpro.eventproapi;

import com.eventpro.eventproapi.entity.Promotion;
import com.eventpro.eventproapi.entity.TypeReduction;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class PromotionTest {
    @Test
    void testCalculReductionPourcentage(){
        Promotion promotion=Promotion.builder()
                .typeReduction(TypeReduction.PERCENTAGE)
                .valeurReduction(new BigDecimal("20"))
                .dateDebut(LocalDateTime.now().minusDays(1))
                .dateFin(LocalDateTime.now().plusDays(1))
                .limiteUtilisation(10)
                .nombreUtilisations(0)
                .build();
        BigDecimal sousTotal=new BigDecimal("1000");
        BigDecimal remise=promotion.calculerReduction(sousTotal);
        assertEquals(new BigDecimal("200"),remise);
    }
}
