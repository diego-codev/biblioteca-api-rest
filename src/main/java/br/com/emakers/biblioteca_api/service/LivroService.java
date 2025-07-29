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
        livro.setNome(livroRequestDTO.getTitulo());
        livro.setAutor(livroRequestDTO.getAutor());
        // Se tiver campo quantidade, adicione aqui
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
}
