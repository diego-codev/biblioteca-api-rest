
package br.com.emakers.biblioteca_api.data.dto.response;
import br.com.emakers.biblioteca_api.data.entity.Livro;



public record LivroResponseDTO(
    Long idLivro,
    String nome,
    String autor
) {
    public LivroResponseDTO(Livro livro) {
        this(
            livro.getIdLivro(),
            livro.getNome(),
            livro.getAutor()
        );
    }

    // Construtor auxiliar para integração externa
    public LivroResponseDTO(String nome, String autor) {
        this(null, nome, autor);
    }
}
