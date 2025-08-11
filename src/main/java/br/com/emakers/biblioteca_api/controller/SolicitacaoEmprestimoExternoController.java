package br.com.emakers.biblioteca_api.controller;

import br.com.emakers.biblioteca_api.data.dto.request.SolicitacaoEmprestimoExternoRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.SolicitacaoEmprestimoExternoResponseDTO;
import br.com.emakers.biblioteca_api.service.SolicitacaoEmprestimoExternoService;
import br.com.emakers.biblioteca_api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Solicitação criada"),
        @ApiResponse(responseCode = "400", description = "Pessoa inexistente")
    })
    public ResponseEntity<?> solicitarEmprestimo(@RequestBody SolicitacaoEmprestimoExternoRequestDTO dto) {
        if (!pessoaService.existsById(dto.getIdPessoa())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pessoa não encontrada para o id informado.");
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
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitação aprovada"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public ResponseEntity<SolicitacaoEmprestimoExternoResponseDTO> aprovarSolicitacao(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprovarSolicitacao(id));
    }

    @PutMapping("/solicitacoes/{id}/rejeitar")
    @Operation(summary = "Rejeita uma solicitação de empréstimo externo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitação rejeitada"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public ResponseEntity<SolicitacaoEmprestimoExternoResponseDTO> rejeitarSolicitacao(@PathVariable Long id) {
        return ResponseEntity.ok(service.rejeitarSolicitacao(id));
    }

    @DeleteMapping("/solicitacoes/{id}")
    @Operation(summary = "Remove uma solicitação de empréstimo externo")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Solicitação removida"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id) {
        service.deletarSolicitacao(id);
        return ResponseEntity.noContent().build();
    }
}