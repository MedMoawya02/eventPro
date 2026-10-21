
package com.eventpro.eventproapi.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "ticket_category")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}