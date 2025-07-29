package br.com.emakers.biblioteca_api.data.dto.request;

public class EmprestimoRequestDTO {
    private Long idLivro;
    private Long idPessoa;

    public Long getIdLivro() {
        return idLivro;
    }
    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }
    public Long getIdPessoa() {
        return idPessoa;
    }
    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }
}
