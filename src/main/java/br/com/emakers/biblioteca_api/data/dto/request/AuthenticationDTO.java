package br.com.emakers.biblioteca_api.data.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
    @NotBlank String login,
    @NotBlank String password
) {}
