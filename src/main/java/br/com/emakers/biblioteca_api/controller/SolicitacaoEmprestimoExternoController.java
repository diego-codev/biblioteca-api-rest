package br.com.emakers.biblioteca_api.controller;

import br.com.emakers.biblioteca_api.data.dto.request.SolicitacaoEmprestimoExternoRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.SolicitacaoEmprestimoExternoResponseDTO;
import br.com.emakers.biblioteca_api.service.SolicitacaoEmprestimoExternoService;
import br.com.emakers.biblioteca_api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import br.com.emakers.biblioteca_api.exception.general.RestErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/emprestimos/externo")
@Tag(name = "Empréstimos Externos", description = "Solicitações de empréstimo externo")
@SecurityRequirement(name = "bearerAuth")
public class SolicitacaoEmprestimoExternoController {
    @Autowired
    private SolicitacaoEmprestimoExternoService service;

    @Autowired
    private PessoaService pessoaService;

    @PostMapping
    @Operation(summary = "Solicita empréstimo externo de livro")
    public ResponseEntity<?> solicitarEmprestimo(@RequestBody SolicitacaoEmprestimoExternoRequestDTO dto) {
    if (!pessoaService.existsById(dto.getIdPessoa())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(RestErrorMessage.of(HttpStatus.BAD_REQUEST, "Pessoa não encontrada para o id informado", "/emprestimos/externo"));
    }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.solicitarEmprestimoExterno(dto));
    }

    @GetMapping("/solicitacoes")
    @Operation(summary = "Lista todas as solicitações de empréstimo externo")
    public ResponseEntity<List<SolicitacaoEmprestimoExternoResponseDTO>> listarSolicitacoes() {
        return ResponseEntity.ok(service.listarSolicitacoes());
    }

    @PutMapping("/solicitacoes/{id}/aprovar")
    @Operation(summary = "Aprova uma solicitação de empréstimo externo")
    public ResponseEntity<SolicitacaoEmprestimoExternoResponseDTO> aprovarSolicitacao(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprovarSolicitacao(id));
    }

    @PutMapping("/solicitacoes/{id}/rejeitar")
    @Operation(summary = "Rejeita uma solicitação de empréstimo externo")
    public ResponseEntity<SolicitacaoEmprestimoExternoResponseDTO> rejeitarSolicitacao(@PathVariable Long id) {
        return ResponseEntity.ok(service.rejeitarSolicitacao(id));
    }

    @DeleteMapping("/solicitacoes/{id}")
    @Operation(summary = "Remove uma solicitação de empréstimo externo")
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id) {
        service.deletarSolicitacao(id);
        return ResponseEntity.noContent().build();
    }
}