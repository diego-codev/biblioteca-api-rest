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
            formatCep(pessoa.getCep()),
            pessoa.getEmail(),
            pessoa.getLogradouro(),
            pessoa.getBairro(),
            pessoa.getLocalidade(),
            pessoa.getUf()
        );
    }

    private static String formatCep(String cep) {
        if (cep == null) return null;
        String digits = cep.replaceAll("\\D", "");
        if (digits.length() == 8) return digits.substring(0,5) + "-" + digits.substring(5);
        return cep;
    }
}
