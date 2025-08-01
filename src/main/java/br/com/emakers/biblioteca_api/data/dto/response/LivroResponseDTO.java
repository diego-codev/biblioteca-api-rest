
package br.com.emakers.biblioteca_api.data.dto.response;


import br.com.emakers.biblioteca_api.data.entity.Livro;
// import java.time.LocalDate; // Removed unused import


public record LivroResponseDTO(
    String nome,
    String autor
) {
    public LivroResponseDTO(Livro livro) {
        this(
            livro.getNome(),
            livro.getAutor()
        );
    }

    // Construtor auxiliar para integração externa
    public LivroResponseDTO(String nome, String autor) {
        this.nome = nome;
        this.autor = autor;
    }
}
