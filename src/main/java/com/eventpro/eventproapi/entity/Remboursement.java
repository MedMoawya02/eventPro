package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "remboursements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Remboursement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Paiement paiement;
    private BigDecimal montant;
    private String motif;
    private LocalDateTime dateDemande;
    @Enumerated(EnumType.STRING)
    private StatutRemboursement statut;

}