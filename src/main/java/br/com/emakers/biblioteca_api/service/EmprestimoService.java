package br.com.emakers.biblioteca_api.service;

import br.com.emakers.biblioteca_api.data.entity.Emprestimo;
import br.com.emakers.biblioteca_api.data.entity.Livro;
import br.com.emakers.biblioteca_api.data.entity.Pessoa;
import br.com.emakers.biblioteca_api.data.dto.request.EmprestimoRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.EmprestimoResponseDTO;
import br.com.emakers.biblioteca_api.repository.EmprestimoRepository;
import br.com.emakers.biblioteca_api.repository.LivroRepository;
import br.com.emakers.biblioteca_api.repository.PessoaRepository;
import br.com.emakers.biblioteca_api.exception.general.ResourceNotFoundException;
import br.com.emakers.biblioteca_api.exception.general.BusinessRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

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
        List<Emprestimo> emprestimos = emprestimoRepository.findAll(Sort.by(Sort.Direction.ASC, "livro.idLivro", "pessoa.idPessoa"));
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    public List<EmprestimoResponseDTO> getEmprestimosAtivosPorPessoa(Long idPessoa) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByPessoaIdPessoaAndDataDevolucaoIsNull(idPessoa)
            .stream().sorted(java.util.Comparator.comparing(e -> e.getLivro().getIdLivro())).toList();
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    public List<EmprestimoResponseDTO> getEmprestimosAtivos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findByDataDevolucaoIsNull()
            .stream().sorted(java.util.Comparator.comparing(e -> e.getLivro().getIdLivro())).toList();
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    public EmprestimoResponseDTO getEmprestimoById(Long idLivro, Long idPessoa) {
        Emprestimo emprestimo = getEmprestimoEntityById(idLivro, idPessoa);
        return new EmprestimoResponseDTO(emprestimo);
    }

    public EmprestimoResponseDTO createEmprestimo(EmprestimoRequestDTO emprestimoRequestDTO) {
        Livro livro = livroRepository.findById(emprestimoRequestDTO.getIdLivro())
            .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: id=" + emprestimoRequestDTO.getIdLivro()));
        if (livro.getQuantidade() == null || livro.getQuantidade() <= 0) {
            throw new BusinessRuleException("Livro indisponível para empréstimo");
        }
        Pessoa pessoa = pessoaRepository.findById(emprestimoRequestDTO.getIdPessoa())
            .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada: id=" + emprestimoRequestDTO.getIdPessoa()));
        // Limite de empréstimos ativos por pessoa
        int emprestimosAtivos = emprestimoRepository.findByPessoaIdPessoaAndDataDevolucaoIsNull(pessoa.getIdPessoa()).size();
        if (emprestimosAtivos >= LIMITE_EMPRESTIMOS_ATIVOS) {
            throw new BusinessRuleException("Limite de empréstimos ativos atingido para esta pessoa");
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


    public void deleteEmprestimo(Long idLivro, Long idPessoa) {
        Emprestimo emprestimo = getEmprestimoEntityById(idLivro, idPessoa);
        emprestimoRepository.delete(emprestimo);
    }

    private Emprestimo getEmprestimoEntityById(Long idLivro, Long idPessoa) {
        return emprestimoRepository.findById(new br.com.emakers.biblioteca_api.data.entity.EmprestimoId(idLivro, idPessoa))
            .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado: livro=" + idLivro + ", pessoa=" + idPessoa));
    }

    // Histórico de empréstimos por pessoa
    public List<EmprestimoResponseDTO> getHistoricoEmprestimosPorPessoa(Long idPessoa) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByPessoaIdPessoa(idPessoa)
            .stream().sorted(java.util.Comparator.comparing(Emprestimo::getDataEmprestimo)).toList();
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    // Histórico de empréstimos por livro
    public List<EmprestimoResponseDTO> getHistoricoEmprestimosPorLivro(Long idLivro) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByLivroIdLivro(idLivro)
            .stream().sorted(java.util.Comparator.comparing(Emprestimo::getDataEmprestimo)).toList();
        return emprestimos.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }

    // Empréstimos atrasados
    public List<EmprestimoResponseDTO> getEmprestimosAtrasados() {
        LocalDate hoje = LocalDate.now();
        List<Emprestimo> atrasados = emprestimoRepository.findByDataPrevistaDevolucaoBeforeAndDataDevolucaoIsNull(hoje)
            .stream().sorted(java.util.Comparator.comparing(Emprestimo::getDataPrevistaDevolucao)).toList();
        return atrasados.stream().map(EmprestimoResponseDTO::new).collect(Collectors.toList());
    }
}
