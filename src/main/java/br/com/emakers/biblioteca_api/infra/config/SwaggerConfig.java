package br.com.emakers.biblioteca_api.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Biblioteca API")
                .version("v1")
                .description("API REST para gerenciamento de biblioteca: CRUD de livros e pessoas, empréstimos, devoluções, autenticação JWT e consulta a API externa ViaCEP/Google Books.")
                .termsOfService("https://github.com/diego-codev/biblioteca-api-rest")
                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                .contact(new Contact().name("Equipe Emakers").email("contato@emakers.com"))
            )
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Servidor Local"),
                new Server().url("https://api.exemplo.com").description("Servidor Produção (exemplo)")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
