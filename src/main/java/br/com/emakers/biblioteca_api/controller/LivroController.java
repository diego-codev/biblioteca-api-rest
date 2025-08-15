package br.com.emakers.biblioteca_api.controller;

import br.com.emakers.biblioteca_api.data.dto.request.LivroRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.LivroResponseDTO;
import br.com.emakers.biblioteca_api.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Operações de CRUD de livros e busca externa")
@SecurityRequirement(name = "bearerAuth")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    @Operation(summary = "Lista todos os livros cadastrados")
    public ResponseEntity<List<LivroResponseDTO>> getAllLivros() {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.getAllLivros());
    }

    @GetMapping("/{idLivro}")
    @Operation(summary = "Busca um livro pelo ID")
    public ResponseEntity<LivroResponseDTO> getLivroById(@PathVariable Long idLivro) {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.getLivroById(idLivro));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo livro")
    public ResponseEntity<LivroResponseDTO> createLivro(@RequestBody @Valid LivroRequestDTO livroRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.createLivro(livroRequestDTO));
    }

    @PutMapping("/{idLivro}")
    @Operation(summary = "Atualiza os dados de um livro pelo ID")
    public ResponseEntity<LivroResponseDTO> updateLivro(@PathVariable Long idLivro, @RequestBody @Valid LivroRequestDTO livroRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.updateLivro(idLivro, livroRequestDTO));
    }

    @DeleteMapping("/{idLivro}")
    @Operation(summary = "Remove um livro pelo ID")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long idLivro) {
        livroService.deleteLivro(idLivro);
        return ResponseEntity.noContent().build();
    }

    // Busca livros na Google Books API
    @GetMapping("/buscar-externo")
    @Operation(summary = "Busca livros externamente na Google Books API")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivrosGoogleBooks(@org.springframework.web.bind.annotation.RequestParam String termo) {
        return ResponseEntity.ok(livroService.buscarLivrosGoogleBooks(termo));
    }
}