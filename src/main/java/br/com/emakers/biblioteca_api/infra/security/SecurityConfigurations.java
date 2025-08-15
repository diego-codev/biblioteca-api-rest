package br.com.emakers.biblioteca_api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfigurations(com.fasterxml.jackson.databind.ObjectMapper objectMapper,
                                  JwtAuthFilter jwtAuthFilter) {
        this.objectMapper = objectMapper;
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationEntryPoint authenticationEntryPoint,
                                                   AccessDeniedHandler accessDeniedHandler) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth
                // Public: documentação e autenticação
                .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/auth/**").permitAll()
                // Público: catálogo de livros (consulta apenas)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/livros/**").permitAll()
                // Protegido (ADMIN): dados de pessoas (contém informações sensíveis)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/pessoas/**").hasRole("ADMIN")
                // Mutação de livros e pessoas: somente ADMIN
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/livros/**", "/pessoas/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/livros/**", "/pessoas/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/livros/**", "/pessoas/**").hasRole("ADMIN")
                // Empréstimos (inclui /emprestimos/externo**): requer usuário autenticado (USER ou ADMIN)
                .requestMatchers("/emprestimos/**").hasAnyRole("ADMIN", "USER")
                // Demais rotas autenticadas
                .anyRequest().authenticated()
            )
            .addFilterBefore(this.jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint jsonAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            var body = br.com.emakers.biblioteca_api.exception.general.RestErrorMessage.of(
                org.springframework.http.HttpStatus.UNAUTHORIZED,
                "Não autenticado ou token inválido.",
                request.getRequestURI()
            );
            response.getWriter().write(objectMapper.writeValueAsString(body));
        };
    }

    @Bean
    public AccessDeniedHandler jsonAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            var body = br.com.emakers.biblioteca_api.exception.general.RestErrorMessage.of(
                org.springframework.http.HttpStatus.FORBIDDEN,
                "Acesso negado.",
                request.getRequestURI()
            );
            response.getWriter().write(objectMapper.writeValueAsString(body));
        };
    }
}
