package com.eventpro.eventproapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@Getter @Setter @NoArgsConstructor @SuperBuilder
public class Organizer extends User {
    private String organisationName;
    public void createEvent() {}
    public void updateEvent() {}
}

