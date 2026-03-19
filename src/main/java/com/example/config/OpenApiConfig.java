package com.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token. Get it from POST /api/auth/login")));
    }

    private Info apiInfo() {
        return new Info()
                .title("AI Study Tracker API")
                .description(
                        "REST API for AI-powered Study Tracker.\n\n" +
                                "## Authentication\n" +
                                "1. Use **POST /api/auth/register** to create an account\n" +
                                "2. Use **POST /api/auth/login** to get a JWT token\n" +
                                "3. Click **Authorize** (🔒) and enter: `<your-token>`\n" +
                                "4. All secured endpoints will now work")
                .version("1.0.0")
                .contact(new Contact()
                        .name("AI Study Tracker")
                        .email("support@aistudytracker.com"))
                .license(new License()
                        .name("MIT License"));
    }
}
