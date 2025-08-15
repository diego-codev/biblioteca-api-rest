package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de registro incluindo endereço preenchido via CEP")
public record RegisterWithAddressResponseDTO(
    @Schema(example = "1") Long id,
    @Schema(example = "joao.silva@example.com") String email,
    @Schema(example = "João Silva") String nome,
    @Schema(example = "USER") UserRole role,
    @Schema(example = "01001-000") String cep,
    @Schema(example = "Praça da Sé") String logradouro,
    @Schema(example = "Sé") String bairro,
    @Schema(example = "São Paulo") String localidade,
    @Schema(example = "SP") String uf
) {
    public static RegisterWithAddressResponseDTO from(
        Long id, String email, String nome, UserRole role,
        String cepRaw, String logradouro, String bairro, String localidade, String uf) {
        return new RegisterWithAddressResponseDTO(id, email, nome, role, formatCep(cepRaw), logradouro, bairro, localidade, uf);
    }

    private static String formatCep(String cep) {
        if (cep == null) return null;
        String digits = cep.replaceAll("\\D", "");
        if (digits.length() == 8) return digits.substring(0,5) + "-" + digits.substring(5);
        return cep; // fallback se já vier formatado
    }
}
