// ...existing code...
package br.com.emakers.biblioteca_api.service;

import br.com.emakers.biblioteca_api.data.dto.request.SolicitacaoEmprestimoExternoRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.SolicitacaoEmprestimoExternoResponseDTO;
import br.com.emakers.biblioteca_api.data.entity.SolicitacaoEmprestimoExterno;
import br.com.emakers.biblioteca_api.data.entity.StatusSolicitacao;
import br.com.emakers.biblioteca_api.repository.SolicitacaoEmprestimoExternoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitacaoEmprestimoExternoService {
    public SolicitacaoEmprestimoExternoResponseDTO rejeitarSolicitacao(Long id) {
        SolicitacaoEmprestimoExterno entity = repository.findById(id).orElseThrow();
        if (entity.getStatus() == StatusSolicitacao.APROVADA) {
            throw new RuntimeException("Solicitação já aprovada, não pode ser rejeitada");
        }
        entity.setStatus(StatusSolicitacao.REJEITADA);
        repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deletarSolicitacao(Long id) {
        repository.deleteById(id);
    }
    @Autowired
    private SolicitacaoEmprestimoExternoRepository repository;

    public SolicitacaoEmprestimoExternoResponseDTO solicitarEmprestimoExterno(SolicitacaoEmprestimoExternoRequestDTO dto) {
        SolicitacaoEmprestimoExterno entity = new SolicitacaoEmprestimoExterno();
        entity.setNomeLivro(dto.getNomeLivro());
        entity.setIdPessoa(dto.getIdPessoa());
        entity.setDataSolicitacao(LocalDate.now());
        entity.setStatus(StatusSolicitacao.PENDENTE);
        repository.save(entity);
        return toResponseDTO(entity);
    }

    public List<SolicitacaoEmprestimoExternoResponseDTO> listarSolicitacoes() {
        return repository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Autowired
    private br.com.emakers.biblioteca_api.repository.LivroRepository livroRepository;
    @Autowired
    private br.com.emakers.biblioteca_api.repository.PessoaRepository pessoaRepository;
    @Autowired
    private br.com.emakers.biblioteca_api.repository.EmprestimoRepository emprestimoRepository;

    public SolicitacaoEmprestimoExternoResponseDTO aprovarSolicitacao(Long id) {
        SolicitacaoEmprestimoExterno entity = repository.findById(id).orElseThrow();
        // Se a solicitação já foi aprovada, impede duplicidade
        if (entity.getStatus() == StatusSolicitacao.APROVADA) {
            throw new RuntimeException("Solicitação já aprovada");
        }
        // 1. Cadastrar o livro no acervo com quantidade 1
        br.com.emakers.biblioteca_api.data.entity.Livro livro = new br.com.emakers.biblioteca_api.data.entity.Livro();
        livro.setNome(entity.getNomeLivro());
        livro.setAutor(entity.getAutor() != null ? entity.getAutor() : "Desconhecido");
        livro.setData_lancamento(entity.getDataLancamento());
        livro.setQuantidade(1); // Começa com 1 exemplar
        livroRepository.save(livro);

        // 2. Criar o empréstimo para a pessoa
        // Se a pessoa não existir, retorna erro
        br.com.emakers.biblioteca_api.data.entity.Pessoa pessoa = pessoaRepository.findById(entity.getIdPessoa())
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        br.com.emakers.biblioteca_api.data.entity.Emprestimo emprestimo = br.com.emakers.biblioteca_api.data.entity.Emprestimo.builder()
            .dto(null)
            .livro(livro)
            .pessoa(pessoa)
            .dataEmprestimo(java.time.LocalDate.now())
            .dataPrevistaDevolucao(java.time.LocalDate.now().plusDays(7))
            .dataDevolucao(null)
            .build();
        // O livro é cadastrado com quantidade 1 e já é emprestado imediatamente (quantidade vai para 0)
        livro.setQuantidade(livro.getQuantidade() - 1);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        // 3. Marcar solicitação como aprovada
        entity.setStatus(StatusSolicitacao.APROVADA);
        repository.save(entity);
        return toResponseDTO(entity);
    }

    private SolicitacaoEmprestimoExternoResponseDTO toResponseDTO(SolicitacaoEmprestimoExterno entity) {
        SolicitacaoEmprestimoExternoResponseDTO dto = new SolicitacaoEmprestimoExternoResponseDTO();
        dto.setId(entity.getId());
        dto.setNomeLivro(entity.getNomeLivro());
        dto.setIdPessoa(entity.getIdPessoa());
        dto.setDataSolicitacao(entity.getDataSolicitacao());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
