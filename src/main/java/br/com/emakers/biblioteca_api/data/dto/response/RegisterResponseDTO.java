package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.UserRole;
public record RegisterResponseDTO(
    Long id,
    String email,
    String nome,
    UserRole role,
    String cep,
    String logradouro,
    String bairro,
    String localidade,
    String uf
) {
    public static RegisterResponseDTO from(
        Long id, String email, String nome, UserRole role,
        String cepRaw, String logradouro, String bairro, String localidade, String uf) {
        return new RegisterResponseDTO(id, email, nome, role, formatCep(cepRaw), logradouro, bairro, localidade, uf);
    }

    private static String formatCep(String cep) {
        if (cep == null) return null;
        String digits = cep.replaceAll("\\D", "");
        if (digits.length() == 8) return digits.substring(0,5) + "-" + digits.substring(5);
        return cep; // fallback se já vier formatado
    }
}
