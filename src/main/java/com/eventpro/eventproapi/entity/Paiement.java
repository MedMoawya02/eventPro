package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "paiements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paiement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String idempotencyKey; // Pour éviter les doubles paiements
    private String reference;
    private BigDecimal montant;
    private LocalDateTime datePaiement;
    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}