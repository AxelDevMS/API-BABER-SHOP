package ams.dev.api.barber_shop.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Barbería")
                        .description("API para gestión de barbería - Sistema de clientes")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("AMS Development")
                                .email("dev@ams.com")
                                .url("https://ams.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación completa")
                        .url("https://wiki.ams.com"));
    }
}