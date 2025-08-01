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
import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> getAllLivros() {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.getAllLivros());
    }

    @GetMapping("/{idLivro}")
    public ResponseEntity<LivroResponseDTO> getLivroById(@PathVariable Long idLivro) {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.getLivroById(idLivro));
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> createLivro(@RequestBody LivroRequestDTO livroRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.createLivro(livroRequestDTO));
    }

    @PutMapping("/{idLivro}")
    public ResponseEntity<LivroResponseDTO> updateLivro(@PathVariable Long idLivro, @RequestBody LivroRequestDTO livroRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.updateLivro(idLivro, livroRequestDTO));
    }

    @DeleteMapping("/{idLivro}")
    public ResponseEntity<String> deleteLivro(@PathVariable Long idLivro) {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.deleteLivro(idLivro));
    }

    // Busca livros na Google Books API
    @GetMapping("/buscar-externo")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivrosGoogleBooks(@org.springframework.web.bind.annotation.RequestParam String termo) {
        return ResponseEntity.ok(livroService.buscarLivrosGoogleBooks(termo));
    }
}