package br.com.emakers.biblioteca_api.data.entity;

import br.com.emakers.biblioteca_api.data.dto.request.EmprestimoRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "emprestimo")
@IdClass(EmprestimoId.class)
public class Emprestimo {
    @Id
    @ManyToOne
    @JoinColumn(name = "idLivro", referencedColumnName = "idLivro")
    private Livro livro;

    @Id
    @ManyToOne
    @JoinColumn(name = "idPessoa", referencedColumnName = "idPessoa")
    private Pessoa pessoa;

    @Column(name = "data_emprestimo")
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Builder
    public Emprestimo(EmprestimoRequestDTO dto, Livro livro, Pessoa pessoa, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        this.livro = livro;
        this.pessoa = pessoa;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }
}
