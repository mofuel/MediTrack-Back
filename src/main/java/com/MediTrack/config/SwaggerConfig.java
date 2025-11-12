package com.MediTrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de MediTrack")
                        .version("1.0.0")
                        .description("Documentación de la API REST de MediTrack con Swagger / OpenAPI")
                        .contact(new Contact()
                                .name("Equipo MediTrack")
                                .email("contacto@meditrack.com")));
    }
}
