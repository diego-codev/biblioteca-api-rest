package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.Pessoa;

public record PessoaResponseDTO(
    Long idPessoa,
    String nome,
    String email,
    String cep,
    String cpf
) {
    public PessoaResponseDTO(Pessoa pessoa) {
        this(
            pessoa.getIdPessoa(),
            pessoa.getNome(),
            pessoa.getEmail(),
            pessoa.getCep(),
            pessoa.getCpf()
        );
    }
}
