package com.fzamiche.back_book_social_network.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    // pour ne pas faire attendre le user pendant l'envoi de l'email, on va l'envoyer en asynchrone avec @Async

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if(emailTemplate == null){
            templateName = "confirm-email";
        }else{
            templateName = emailTemplate.name();
        }

        // Creation du MimeMessage pour representer un message email  et MimeMessageHelper pour faciliter la creation du message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );

        // Creation des proprietes pour le template
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activationCode", activationCode);

        // Creation du contexte pour le template
        Context context = new Context();
        context.setVariables(properties);

        // Configuration des détails du message
        helper.setFrom("contact@fatah-zamiche.com");
        helper.setTo(to);
        helper.setSubject(subject);

        // Traitement du template avec le contexte pour generer le contenu du message
        String template = templateEngine.process(templateName, context);
        // Configuration du contenu du message avec le template traité et le type de contenu
        helper.setText(template, true);

        // Envoi du message
        javaMailSender.send(mimeMessage);

    }
}
