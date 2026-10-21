package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "billets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Billet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code; // Code unique généré
    private LocalDateTime dateEmission;
    private LocalDateTime dateControle;
    @Enumerated(EnumType.STRING)
    private StatutBillet statut;
    @ManyToOne
    @JoinColumn(name = "ligne_reservation_id")
    private LigneReservation ligneReservation;
    private LocalDateTime issueDate;
    private LocalDateTime scanDate;

    public boolean valider() {
        if (this.statut == StatutBillet.VALID) return true;
        return false;
    }
    public boolean scan() {
        if (this.statut == StatutBillet.VALID) {
            this.statut = StatutBillet.USED;
            this.scanDate = LocalDateTime.now();
            return true;
        }
        return false;
    }
}