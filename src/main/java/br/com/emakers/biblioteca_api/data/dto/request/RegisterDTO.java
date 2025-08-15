
package br.com.emakers.biblioteca_api.data.dto.request;

import br.com.emakers.biblioteca_api.data.entity.UserRole;
import jakarta.validation.constraints.*;

public record RegisterDTO(
	@NotBlank(message = "Nome é obrigatório")
	String nome,
	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email deve ser válido")
	String email,
	@NotBlank(message = "CEP é obrigatório")
	@Pattern(regexp = "(\\d{5}-\\d{3})|(\\d{8})", message = "CEP deve estar no formato 00000-000 ou 8 dígitos")
	String cep,
	@NotBlank(message = "CPF é obrigatório")
	@Pattern(regexp = "(\\d{11})|(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})", message = "CPF deve ter 11 dígitos ou formato 000.000.000-00")
	String cpf,
	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 6, message = "Senha deve ter ao menos 6 caracteres")
	String password,
	UserRole role
) {}
