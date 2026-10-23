package com.eventpro.eventproapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;             // Si Client
    private String organisationName;  // Si Organisateur
}
