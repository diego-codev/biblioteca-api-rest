package br.com.emakers.biblioteca_api.service;
import br.com.emakers.biblioteca_api.data.dto.request.LivroRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.LivroResponseDTO;
import br.com.emakers.biblioteca_api.repository.LivroRepository;
import br.com.emakers.biblioteca_api.data.entity.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;

    public List<LivroResponseDTO> getAllLivros() {
        List<Livro> livros = livroRepository.findAll();
        return livros.stream().map(LivroResponseDTO::new).collect(Collectors.toList());
    }


    public LivroResponseDTO getLivroById(Long idLivro) {
        Livro livro = getLivroEntityById(idLivro);
        return new LivroResponseDTO(livro);
    }

    public LivroResponseDTO createLivro(LivroRequestDTO livroRequestDTO) {
        Livro livro = new Livro(livroRequestDTO);
        livroRepository.save(livro);
        return new LivroResponseDTO(livro);
    }


    public LivroResponseDTO updateLivro(Long idLivro, LivroRequestDTO livroRequestDTO) {
        Livro livro = getLivroEntityById(idLivro);
        livro.setNome(livroRequestDTO.getNome());
        livro.setAutor(livroRequestDTO.getAutor());
        // ...
        livroRepository.save(livro);
        return new LivroResponseDTO(livro);
    }


    public String deleteLivro(Long idLivro) {
        Livro livro = getLivroEntityById(idLivro);
        livroRepository.delete(livro);
        return "Livro id: " + idLivro + " deletado!";
    }

    private Livro getLivroEntityById(Long idLivro) {
        return livroRepository.findById(idLivro)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }

    // Busca livros na Google Books API
    public List<LivroResponseDTO> buscarLivrosGoogleBooks(String termo) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=" + java.net.URLEncoder.encode(termo, java.nio.charset.StandardCharsets.UTF_8);
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(java.net.URI.create(url)).build();
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response.body());
            java.util.List<LivroResponseDTO> livros = new java.util.ArrayList<>();
            if (root.has("items")) {
                for (com.fasterxml.jackson.databind.JsonNode item : root.get("items")) {
                    com.fasterxml.jackson.databind.JsonNode volumeInfo = item.get("volumeInfo");
                    String nome = volumeInfo.has("title") ? volumeInfo.get("title").asText("") : "";
                    String autor = (volumeInfo.has("authors") && volumeInfo.get("authors").isArray() && volumeInfo.get("authors").size() > 0)
                        ? volumeInfo.get("authors").get(0).asText("") : "";
                    livros.add(new LivroResponseDTO(nome, autor));
                }
            }
            return livros;
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }
}
