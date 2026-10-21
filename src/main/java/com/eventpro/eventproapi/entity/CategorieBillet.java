package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "categories_billet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorieBillet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;

    // ⬇️ C'EST CE CHAMP QUI MANQUAIT ⬇️
    @ManyToOne
    @JoinColumn(name = "category_id")
    private TicketCategory category;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    @Column(nullable = false)
    private int quantiteTotale;

    @Column(nullable = false)
    private int quotaMaxParClient;

    @Column(nullable = false)
    private int placesReservees = 0;

    @Column(nullable = false)
    private int placesVendues = 0;

    public int getPlacesDisponibles() {
        return quantiteTotale - (placesReservees + placesVendues);
    }

    public boolean verifierStock(int quantite) {
        return getPlacesDisponibles() >= quantite;
    }

    public void reserver(int quantite) {
        this.placesReservees += quantite;
    }

    public void vendre(int quantite) {
        this.placesReservees -= quantite;
        this.placesVendues += quantite;
    }

    public void liberer(int quantite) {
        this.placesReservees -= quantite;
    }
}