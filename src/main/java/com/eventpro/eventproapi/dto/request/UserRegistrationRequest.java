package com.eventpro.eventproapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegistrationRequest {
@NotBlank(message = "Le nom est requis")
    private String name;
@NotBlank(message = "l'email est requis")
@Email(message = "Format d'emailinvalide")
private   String email;
@NotNull(message = "Le role est requis")
    private String role;
//champ pour client
private String phone;
//champs pour l'organisateur
private String organisationName;
}
