package com.fzamiche.back_book_social_network.auth.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Builder
public class RegistrationRequest {

    private static final String REQUIRED_FIELD_MESSAGE = "Ce champ est requis.";
    private static final String INVALID_EMAIL_MESSAGE = "Email non valide.";
    private static final String PASSWORD_TOO_SHORT_MESSAGE = "Mot de passe trop court, minimum 8 caractères.";

    @NotBlank(message = REQUIRED_FIELD_MESSAGE)
    private String firstname;

    @NotBlank(message = REQUIRED_FIELD_MESSAGE)
    private String lastname;

    @NotBlank(message = REQUIRED_FIELD_MESSAGE)
    @Email(message = INVALID_EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = REQUIRED_FIELD_MESSAGE)
    @Size(min = 8, message = PASSWORD_TOO_SHORT_MESSAGE)
    private String password;
}
