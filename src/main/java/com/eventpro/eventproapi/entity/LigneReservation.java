package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lignes_reservation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieBillet categorieBillet;

    public BigDecimal calculerSousTotal() {
        return prixUnitaire.multiply(new BigDecimal(quantite));
    }
}