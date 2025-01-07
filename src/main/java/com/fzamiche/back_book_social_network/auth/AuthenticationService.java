package com.fzamiche.back_book_social_network.auth;

import com.fzamiche.back_book_social_network.auth.dto.AuthenticationRequest;
import com.fzamiche.back_book_social_network.auth.dto.AuthenticationResponse;
import com.fzamiche.back_book_social_network.auth.dto.RegistrationRequest;
import com.fzamiche.back_book_social_network.email.EmailService;
import com.fzamiche.back_book_social_network.email.EmailTemplateName;
import com.fzamiche.back_book_social_network.role.Role;
import com.fzamiche.back_book_social_network.role.RoleRepository;
import com.fzamiche.back_book_social_network.security.JwtService;
import com.fzamiche.back_book_social_network.user.token.Token;
import com.fzamiche.back_book_social_network.user.token.TokenRepository;
import com.fzamiche.back_book_social_network.user.User;
import com.fzamiche.back_book_social_network.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
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
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
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
        return Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expriredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
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

    @Transactional //pour que la transaction soit commitée à la fin de la méthode, 1. Simplifie la gestion des transactions en automatique. 2. Garantit que plusieurs opérations de base de données soient exécutées ensemble (ou pas du tout en cas d'échec).
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo - gérer les exceptions de manière plus propre
                .orElseThrow(() -> new RuntimeException("Token non trouvé"));

        // si le token est expiré, on envoie un nouveau code d'activation
        if(LocalDateTime.now().isAfter(savedToken.getExpriredAt())){
            sendValidationEmail(savedToken.getUser());
            // todo - gérer les exceptions de manière plus propre
            throw new RuntimeException("Code d'activation de votre compte expiré, un autre code a été envoyé à votre adresse email." + savedToken.getUser().getEmail());
        }

        // activer le compte, doubler la sécurité en vérifiant que l'utilisateur existe bien en base, on aurait du recupr le user depuis le savedToken.getUser()
        User user = userRepository.findByEmail(savedToken.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur n'a pas été trouvé"));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
