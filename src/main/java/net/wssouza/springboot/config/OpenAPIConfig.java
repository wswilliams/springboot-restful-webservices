package net.wssouza.springboot.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${desafio.openapi.dev-url}")
    private String devUrl;

    @Value("${desafio.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("wswilliamssilva938@gmail.com");
        contact.setName("wswilliams");
        contact.setUrl("");

        Info info = new Info()
                .title("Desafio Luizalabs Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage desafio.");

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
