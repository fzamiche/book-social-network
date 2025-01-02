package com.fzamiche.back_book_social_network.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "Pas de code d'erreur métier"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Le mot de passe actuel est incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "Le nouveau mot de passe ne correspond pas à la confirmation"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "Le compte utilisateur est verrouillé"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "Le compte utilisateur est désactivé"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Les identifiants sont incorrects"),
    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
