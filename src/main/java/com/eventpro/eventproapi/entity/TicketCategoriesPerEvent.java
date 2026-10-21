package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket_categories_per_event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketCategoriesPerEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Evenement event;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TicketCategory category;

    private BigDecimal unitPrice;
    private int totalQuantity;
    private int maxQuotaPerClient;

    // Champs ajoutés pour gérer le stock (indispensables)
    @Builder.Default
    private int reservedQuantity = 0;

    @Builder.Default
    private int soldQuantity = 0;

    public boolean checkStock(int quantity) {
        return (totalQuantity - (reservedQuantity + soldQuantity)) >= quantity;
    }

    public int getAvailablePlaces() {
        return totalQuantity - (reservedQuantity + soldQuantity);
    }
}