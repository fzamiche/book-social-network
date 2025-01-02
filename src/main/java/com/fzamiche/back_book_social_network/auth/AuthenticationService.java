package com.fzamiche.back_book_social_network.auth;

import com.fzamiche.back_book_social_network.email.EmailService;
import com.fzamiche.back_book_social_network.email.EmailTemplateName;
import com.fzamiche.back_book_social_network.role.Role;
import com.fzamiche.back_book_social_network.role.RoleRepository;
import com.fzamiche.back_book_social_network.security.JwtService;
import com.fzamiche.back_book_social_network.user.Token;
import com.fzamiche.back_book_social_network.user.TokenRepository;
import com.fzamiche.back_book_social_network.user.User;
import com.fzamiche.back_book_social_network.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String acttivationUrl;

    public void register(RegistrationRequest request) throws MessagingException {

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

    private void sendValidationEmail(User user) throws MessagingException {
        // generer un token d'activation (code de 6 chiffres) et l'enregistrer en base
        var newToken = generateAndSaveActivationToken(user);

        // envoyer un email avec le token
        emailService.sendEmail(
                user.getEmail(),
                user.getFirstname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                acttivationUrl,
                newToken,
                "Activation de votre compte"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        // generer un code d'activation
        String generatedToken = generateActivationToken(6);
        var token = createToken(user, generatedToken);

        // enregistrer le token en base
        tokenRepository.save(token);
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullname", user.getFullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }
}
