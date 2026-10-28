package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evenements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    private String description;
    private String lieu;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private LocalDateTime dateOuvertureVente;
    private LocalDateTime dateClotureVente;
    @Column(nullable = false)
    private int capacite;
    @Enumerated(EnumType.STRING)
    private StatutEvenement statut;

    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private Organizer organisateur;

    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL)
    private List<CategorieBillet> categories;

    public void publier() { this.statut = StatutEvenement.PUBLISHED; }
    public void annuler() { this.statut = StatutEvenement.CANCELLED; }
    public boolean verifierDisponibilite() { return true; }
}