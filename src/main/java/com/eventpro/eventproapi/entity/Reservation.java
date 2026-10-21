package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private StatutReservation status;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationLine> lines;

    public BigDecimal calculateTotal() { return totalAmount; }
    public void confirm() { this.status = StatutReservation.CONFIRMED; }
    public void cancel() { this.status = StatutReservation.CANCELLED; }
    public void expire() { this.status = StatutReservation.EXPIRED; }
}