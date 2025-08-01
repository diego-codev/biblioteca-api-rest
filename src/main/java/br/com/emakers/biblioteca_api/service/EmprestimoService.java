

// Histórico de empréstimos por pessoa
// (métodos estavam fora da classe, corrigido)
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


    private static final int LIMITE_EMPRESTIMOS_ATIVOS = 3;

    public List<EmprestimoResponseDTO> getAllEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    public List<EmprestimoResponseDTO> getEmprestimosAtivosPorPessoa(Long idPessoa) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByPessoaIdPessoaAndDataDevolucaoIsNull(idPessoa);
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    public List<EmprestimoResponseDTO> getEmprestimosAtivos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findByDataDevolucaoIsNull();
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    public EmprestimoResponseDTO getEmprestimoById(Long idLivro, Long idPessoa) {
        Emprestimo emprestimo = getEmprestimoEntityById(idLivro, idPessoa);
        return new EmprestimoResponseDTO(emprestimo);
    }

    public EmprestimoResponseDTO createEmprestimo(EmprestimoRequestDTO emprestimoRequestDTO) {
        Livro livro = livroRepository.findById(emprestimoRequestDTO.getIdLivro())
            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        if (livro.getQuantidade() == null || livro.getQuantidade() <= 0) {
            throw new RuntimeException("Livro indisponível para empréstimo");
        }
        Pessoa pessoa = pessoaRepository.findById(emprestimoRequestDTO.getIdPessoa())
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        // Limite de empréstimos ativos por pessoa
        int emprestimosAtivos = emprestimoRepository.findByPessoaIdPessoaAndDataDevolucaoIsNull(pessoa.getIdPessoa()).size();
        if (emprestimosAtivos >= LIMITE_EMPRESTIMOS_ATIVOS) {
            throw new RuntimeException("Limite de empréstimos ativos atingido para esta pessoa");
        }
        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevistaDevolucao = hoje.plusDays(7);
        Emprestimo emprestimo = Emprestimo.builder()
            .dto(emprestimoRequestDTO)
            .livro(livro)
            .pessoa(pessoa)
            .dataEmprestimo(hoje)
            .dataPrevistaDevolucao(dataPrevistaDevolucao)
            .dataDevolucao(null)
            .build();
        // Decrementa quantidade
        livro.setQuantidade(livro.getQuantidade() - 1);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);
        return new EmprestimoResponseDTO(emprestimo);
    }


    public EmprestimoResponseDTO updateEmprestimo(Long idLivro, Long idPessoa) {
        Emprestimo emprestimo = getEmprestimoEntityById(idLivro, idPessoa);
        // Não aceita dataDevolucao do usuário, apenas registra a devolução se ainda não devolvido
        if (emprestimo.getDataDevolucao() == null) {
            emprestimo.setDataDevolucao(LocalDate.now());
            // Incrementa quantidade ao devolver
            Livro livro = emprestimo.getLivro();
            livro.setQuantidade(livro.getQuantidade() + 1);
            livroRepository.save(livro);
        }
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

    // Histórico de empréstimos por pessoa
    public List<EmprestimoResponseDTO> getHistoricoEmprestimosPorPessoa(Long idPessoa) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByPessoaIdPessoa(idPessoa);
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    // Histórico de empréstimos por livro
    public List<EmprestimoResponseDTO> getHistoricoEmprestimosPorLivro(Long idLivro) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByLivroIdLivro(idLivro);
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    // Empréstimos atrasados
    public List<EmprestimoResponseDTO> getEmprestimosAtrasados() {
        LocalDate hoje = LocalDate.now();
        List<Emprestimo> atrasados = emprestimoRepository.findByDataPrevistaDevolucaoBeforeAndDataDevolucaoIsNull(hoje);
        return atrasados.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }
}
