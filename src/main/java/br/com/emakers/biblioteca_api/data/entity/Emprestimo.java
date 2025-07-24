package br.com.emakers.biblioteca_api.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
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
}
