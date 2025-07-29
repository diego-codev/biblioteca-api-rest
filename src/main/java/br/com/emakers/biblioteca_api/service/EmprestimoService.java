package br.com.emakers.biblioteca_api.service;

import br.com.emakers.biblioteca_api.data.entity.Emprestimo;
import br.com.emakers.biblioteca_api.data.entity.Livro;
import br.com.emakers.biblioteca_api.data.entity.Pessoa;
import br.com.emakers.biblioteca_api.data.dto.request.EmprestimoRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.EmprestimoResponseDTO;
import br.com.emakers.biblioteca_api.repository.EmprestimoRepository;
import br.com.emakers.biblioteca_api.repository.LivroRepository;
import br.com.emakers.biblioteca_api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmprestimoService {
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    public List<EmprestimoResponseDTO> getAllEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }


    public EmprestimoResponseDTO getEmprestimoById(Long idLivro, Long idPessoa) {
        Emprestimo emprestimo = getEmprestimoEntityById(idLivro, idPessoa);
        return new EmprestimoResponseDTO(emprestimo);
    }

    public EmprestimoResponseDTO createEmprestimo(EmprestimoRequestDTO emprestimoRequestDTO) {
        Livro livro = livroRepository.findById(emprestimoRequestDTO.getIdLivro())
            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        Pessoa pessoa = pessoaRepository.findById(emprestimoRequestDTO.getIdPessoa())
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        Emprestimo emprestimo = Emprestimo.builder()
            .dto(emprestimoRequestDTO)
            .livro(livro)
            .pessoa(pessoa)
            .dataEmprestimo(LocalDate.now())
            .dataDevolucao(null)
            .build();
        emprestimoRepository.save(emprestimo);
        return new EmprestimoResponseDTO(emprestimo);
    }


    public EmprestimoResponseDTO updateEmprestimo(Long idLivro, Long idPessoa, EmprestimoRequestDTO emprestimoRequestDTO) {
        Emprestimo emprestimo = getEmprestimoEntityById(idLivro, idPessoa);
        // Exemplo: atualizar dataDevolucao ao devolver o livro
        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimoRepository.save(emprestimo);
        return new EmprestimoResponseDTO(emprestimo);
    }


    public String deleteEmprestimo(Long idLivro, Long idPessoa) {
        Emprestimo emprestimo = getEmprestimoEntityById(idLivro, idPessoa);
        emprestimoRepository.delete(emprestimo);
        return "Empréstimo Livro id: " + idLivro + ", Pessoa id: " + idPessoa + " deletado!";
    }

    private Emprestimo getEmprestimoEntityById(Long idLivro, Long idPessoa) {
        return emprestimoRepository.findById(new br.com.emakers.biblioteca_api.data.entity.EmprestimoId(idLivro, idPessoa))
            .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
    }
}
