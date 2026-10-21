package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@Getter @Setter @NoArgsConstructor @SuperBuilder
public class Client extends User {
    private String phone;
}