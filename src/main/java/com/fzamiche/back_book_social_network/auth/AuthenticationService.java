package com.fzamiche.back_book_social_network.auth;

import com.fzamiche.back_book_social_network.role.Role;
import com.fzamiche.back_book_social_network.role.RoleRepository;
import com.fzamiche.back_book_social_network.user.Token;
import com.fzamiche.back_book_social_network.user.TokenRepository;
import com.fzamiche.back_book_social_network.user.User;
import com.fzamiche.back_book_social_network.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRespositoty;

    public void register(RegistrationRequest request) {

        var userRole = roleRepository.findByName("USER")
                // todo - gérer les exceptions de manière plus propre
                .orElseThrow(() -> new IllegalStateException("Role USER n'est pas initialisé."));

        var user = createUser(request, userRole);
        userRepository.save(user);
        sendValidationEmail(user);

    }

    private User createUser(RegistrationRequest request, Role userRole) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        return user;
    }

    private void sendValidationEmail(User user) {
        // generer un token d'activation (code de 6 chiffres) et l'enregistrer en base
        var newToken = generateAndSaveActivationToken(user);
        // envoyer un email avec le token
        // todo - envoyer un email
    }

    private String generateAndSaveActivationToken(User user) {
        // generer un code d'activation
        String generatedToken = generateActivationToken(6);
        var token = createToken(user, generatedToken);

        // enregistrer le token en base
        tokenRespositoty.save(token);
        return generatedToken;
    }

    private static Token createToken(User user, String generatedToken) {
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expriredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        return token;
    }

    private String generateActivationToken(int lengthDigits) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0; i < lengthDigits; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
