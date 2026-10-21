package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@Getter @Setter @NoArgsConstructor @SuperBuilder
public class ControlAgent extends User {
    public boolean verifyTicket(String code) { return false; }
}