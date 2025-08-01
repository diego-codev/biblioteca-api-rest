package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.StatusSolicitacao;

import java.time.LocalDate;

public class SolicitacaoEmprestimoExternoResponseDTO {
    private Long id;
    private String nomeLivro;
    private Long idPessoa;
    private LocalDate dataSolicitacao;
    private StatusSolicitacao status;

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeLivro() { return nomeLivro; }
    public void setNomeLivro(String nomeLivro) { this.nomeLivro = nomeLivro; }
    public Long getIdPessoa() { return idPessoa; }
    public void setIdPessoa(Long idPessoa) { this.idPessoa = idPessoa; }
    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
    public StatusSolicitacao getStatus() { return status; }
    public void setStatus(StatusSolicitacao status) { this.status = status; }
}
