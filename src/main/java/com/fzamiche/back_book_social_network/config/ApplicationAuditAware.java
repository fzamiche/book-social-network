package com.fzamiche.back_book_social_network.config;


import com.fzamiche.back_book_social_network.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


/**
 * Cette classe permet de gérer les champs createdBy et lastModifiedBy
 * à partir de SecurityContextHolder on récupère l'utilisateur connecté
 * et on retourne son id
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of(0);
        }
        User userPrincipal =  (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId()); // ofNullable pour éviter les nullPointerException, si l'utilisateur n'est pas connecté on retourne 0
    }
}
