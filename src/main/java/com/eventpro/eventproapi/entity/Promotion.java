package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Evenement evenement;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private TypeReduction typeReduction;

    private BigDecimal valeurReduction;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    private int limiteUtilisation;
    private int nombreUtilisations = 0;

    public boolean estValide() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(dateDebut) && now.isBefore(dateFin) && nombreUtilisations < limiteUtilisation;
    }

    public BigDecimal calculerReduction(BigDecimal sousTotal) {
        if (!estValide()) return BigDecimal.ZERO;

        if (typeReduction == TypeReduction.PERCENTAGE) {
            return sousTotal.multiply(valeurReduction).divide(new BigDecimal("100"));
        } else {
            return valeurReduction.min(sousTotal);
        }
    }
}