package com.fzamiche.back_book_social_network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // pour activer l'auditeur JPA
public class BackBookSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackBookSocialNetworkApplication.class, args);
	}

}
