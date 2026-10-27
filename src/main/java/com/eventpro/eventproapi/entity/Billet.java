package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "billets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // Code unique généré (ex: TKT-XXXX)

    private LocalDateTime dateEmission; // Date de création du billet
    private LocalDateTime dateControle; // Date du scan à l'entrée

    @Enumerated(EnumType.STRING)
    private StatutBillet statut;

    @ManyToOne
    @JoinColumn(name = "ligne_reservation_id")
    private LigneReservation ligneReservation;

    // Méthode métier : vérifier si le billet est valide
    public boolean valider() {
        return this.statut == StatutBillet.VALID;
    }

    // Méthode métier : scanner le billet à l'entrée
    public boolean scan() {
        if (this.statut == StatutBillet.VALID) {
            this.statut = StatutBillet.USED;
            this.dateControle = LocalDateTime.now();
            return true;
        }
        return false;
    }
}