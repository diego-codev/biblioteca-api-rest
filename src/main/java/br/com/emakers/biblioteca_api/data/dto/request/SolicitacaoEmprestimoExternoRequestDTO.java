package br.com.emakers.biblioteca_api.data.dto.request;

public class SolicitacaoEmprestimoExternoRequestDTO {
    private String nomeLivro;
    private Long idPessoa;

    // Getters e setters
    public String getNomeLivro() { return nomeLivro; }
    public void setNomeLivro(String nomeLivro) { this.nomeLivro = nomeLivro; }
    public Long getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Long idPessoa) { this.idPessoa = idPessoa; }
}
