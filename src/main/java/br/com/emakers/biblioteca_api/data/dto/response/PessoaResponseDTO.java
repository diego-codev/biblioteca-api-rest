package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.Pessoa;

public record PessoaResponseDTO(
    Long idPessoa,
    String nome,
    String cpf,
    String cep,
    String email,
    String logradouro,
    String bairro,
    String localidade,
    String uf
) {
    public PessoaResponseDTO(Pessoa pessoa) {
        this(
            pessoa.getIdPessoa(),
            pessoa.getNome(),
            pessoa.getCpf(),
            pessoa.getCep(),
            pessoa.getEmail(),
            pessoa.getLogradouro(),
            pessoa.getBairro(),
            pessoa.getLocalidade(),
            pessoa.getUf()
        );
    }
}
