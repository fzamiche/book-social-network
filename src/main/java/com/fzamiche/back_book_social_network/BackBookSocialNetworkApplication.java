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
@EnableJpaAuditing  // pour activer l'auditeur JPA
@EnableAsync 	  // pour activer l'envoi d'email en asynchrone
public class BackBookSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackBookSocialNetworkApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			if(roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(
						Role.builder()
								.name("USER")
								.build()
				);
			}
		};
	}
}
