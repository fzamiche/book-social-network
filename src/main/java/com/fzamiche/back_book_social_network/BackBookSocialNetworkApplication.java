package com.fzamiche.back_book_social_network;

import com.fzamiche.back_book_social_network.role.Role;
import com.fzamiche.back_book_social_network.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
/*
	Pour activer l'auditeur JPA, qui permet de gérer les champs createdDate, lastModifiedDate,
	Et pour les champs createdBy et lastModifiedBy, il faut implémenter l'interface AuditorAware.
*/
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // on précise le bean qui va gérer les champs createdBy et lastModifiedBy
@EnableAsync      // pour activer l'envoi d'email en asynchrone
public class BackBookSocialNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackBookSocialNetworkApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(
                        Role.builder()
                                .name("USER")
                                .build()
                );
            }
        };
    }
}
