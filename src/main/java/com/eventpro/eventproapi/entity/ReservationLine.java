package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationLine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "ticket_category_per_event_id")
    private CategorieBillet category;

    private int quantity;
    private BigDecimal fixedUnitPrice;
    private BigDecimal subtotal;

    public BigDecimal calculateSubtotal() {
        return fixedUnitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}