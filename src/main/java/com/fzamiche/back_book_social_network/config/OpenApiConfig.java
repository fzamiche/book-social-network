package com.fzamiche.back_book_social_network.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
            contact = @Contact(
                    name = "fzamiche",
                    email = "contact.fatah.zamiche@gmail.com"
            ),
            description = "OpenAPi documentation for Spring security",
            title = "Open specification - fzamiche",
            version = "1.0",
            termsOfService = "Terms of service"
    ),
        servers = {
            @Server(
                    description = "Local ENV",
                    url = "http://localhost:8088/api/v1"
            ),
                @Server(
                        description = "Prod ENV",
                        url = "http://fzamiche"
                )
        },
        security = {
            @SecurityRequirement(
                  name = "bearerAuth"
            )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
