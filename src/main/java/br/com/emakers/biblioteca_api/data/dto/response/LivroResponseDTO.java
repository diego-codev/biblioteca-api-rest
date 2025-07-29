package br.com.emakers.biblioteca_api.data.dto.response;

import br.com.emakers.biblioteca_api.data.entity.Livro;
import java.util.Date;

public record LivroResponseDTO(
    Long idLivro,
    String nome,
    String autor,
    Date dataLancamento
) {
    public LivroResponseDTO(Livro livro) {
        this(
            livro.getIdLivro(),
            livro.getNome(),
            livro.getAutor(),
            livro.getData_lancamento()
        );
    }
}
