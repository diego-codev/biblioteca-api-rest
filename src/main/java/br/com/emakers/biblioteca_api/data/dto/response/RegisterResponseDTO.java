package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.UserRole;

public record RegisterResponseDTO(Long id, String email, UserRole role) {}
