
package br.com.emakers.biblioteca_api.data.dto.request;

import br.com.emakers.biblioteca_api.data.entity.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {}
