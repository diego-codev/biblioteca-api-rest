package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.Pessoa;

public record PessoaResponseDTO(
    Long idPessoa,
    String nome,
    String cpf,
    String cep,
    String email
) {
    public PessoaResponseDTO(Pessoa pessoa) {
        this(
            pessoa.getIdPessoa(),
            pessoa.getNome(),
            pessoa.getCpf(),
            pessoa.getCep(),
            pessoa.getEmail()
        );
    }
}
