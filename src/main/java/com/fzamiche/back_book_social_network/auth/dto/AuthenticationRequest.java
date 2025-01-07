package com.fzamiche.back_book_social_network.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    private static final String INVALID_EMAIL_MESSAGE = "Email non valide.";
    private static final String PASSWORD_TOO_SHORT_MESSAGE = "Mot de passe trop court, minimum 8 caractères.";

    @NotBlank(message = "Le champs firstname est requis")
    @Email(message = INVALID_EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = "Le password firstname est requis")
    @Size(min = 8, message = PASSWORD_TOO_SHORT_MESSAGE)
    private String password;
}
