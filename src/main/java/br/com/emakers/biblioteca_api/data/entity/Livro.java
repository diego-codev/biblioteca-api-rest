package br.com.emakers.biblioteca_api.data.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import br.com.emakers.biblioteca_api.data.dto.request.LivroRequestDTO;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idLivro;
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    @Column(name = "autor", nullable = false, length = 100)
    private String autor;
    @Column(name = "data_lancamento")
    private LocalDate data_lancamento;
    @Column(name = "quantidade")
    private Integer quantidade;

    @Builder
    public Livro(LivroRequestDTO dto) {
        this.nome = dto.getNome();
        this.autor = dto.getAutor();
        this.quantidade = dto.getQuantidade();
        this.data_lancamento = dto.getDataLancamento();
    }

        public void setDataLancamento(LocalDate dataLancamento) {
            this.data_lancamento = dataLancamento;
        }

}
