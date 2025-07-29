package br.com.emakers.biblioteca_api.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PessoaRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @Pattern(regexp = "(\\d{5}-\\d{3})|(\\d{8})", message = "CEP deve estar no formato 00000-000 ou 8 dígitos")
    private String cep;

    @Pattern(regexp = "(\\d{11})|(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})", message = "CPF deve ter 11 dígitos ou estar no formato 000.000.000-00")
    private String cpf;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
