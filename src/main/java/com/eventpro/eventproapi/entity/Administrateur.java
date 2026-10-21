package com.eventpro.eventproapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "administrateurs")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Administrateur extends User {

    public void gererUtilisateurs() {
        // Logique métier
    }
}