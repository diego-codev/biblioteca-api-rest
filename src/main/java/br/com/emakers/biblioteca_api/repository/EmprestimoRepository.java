package br.com.emakers.biblioteca_api.repository;

import br.com.emakers.biblioteca_api.data.entity.Emprestimo;
import br.com.emakers.biblioteca_api.data.entity.EmprestimoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, EmprestimoId> {
    List<Emprestimo> findByPessoaIdPessoaAndDataDevolucaoIsNull(Long idPessoa);
    List<Emprestimo> findByDataDevolucaoIsNull();

    // Histórico de empréstimos por pessoa
    List<Emprestimo> findByPessoaIdPessoa(Long idPessoa);

    // Histórico de empréstimos por livro
    List<Emprestimo> findByLivroIdLivro(Long idLivro);

    // Empréstimos atrasados
    List<Emprestimo> findByDataPrevistaDevolucaoBeforeAndDataDevolucaoIsNull(LocalDate data);
}
