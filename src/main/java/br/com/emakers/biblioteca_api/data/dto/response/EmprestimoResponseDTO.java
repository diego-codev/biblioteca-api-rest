package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.Emprestimo;
import java.time.LocalDate;

public record EmprestimoResponseDTO(
    Long idLivro,
    Long idPessoa,
    LocalDate dataEmprestimo,
    LocalDate dataDevolucao
) {
    public EmprestimoResponseDTO(Emprestimo emprestimo) {
        this(
            emprestimo.getLivro() != null ? emprestimo.getLivro().getIdLivro() : null,
            emprestimo.getPessoa() != null ? emprestimo.getPessoa().getIdPessoa() : null,
            emprestimo.getDataEmprestimo(),
            emprestimo.getDataDevolucao()
        );
    }
}
