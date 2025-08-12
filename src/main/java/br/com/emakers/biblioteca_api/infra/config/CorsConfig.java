package br.com.emakers.biblioteca_api.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000") // Ajustar conforme necessário
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
            .allowedHeaders("Authorization", "Content-Type", "Accept")
            .exposedHeaders("Authorization")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
