package br.com.emakers.biblioteca_api.data.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    @Temporal(TemporalType.DATE)
    private Date data_lancamento;

    @Builder
    public Livro(LivroRequestDTO dto) {
        this.nome = dto.getTitulo();
        this.autor = dto.getAutor();
        // quantidade não existe na entidade, ajuste se necessário
    }
}
