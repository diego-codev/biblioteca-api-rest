package br.com.emakers.biblioteca_api.controller;
import br.com.emakers.biblioteca_api.data.dto.request.EmprestimoRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.EmprestimoResponseDTO;
import br.com.emakers.biblioteca_api.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimos", description = "Operações de empréstimo, devolução e histórico")
@SecurityRequirement(name = "bearerAuth")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    @Operation(summary = "Lista todos os empréstimos cadastrados")
    public ResponseEntity<List<EmprestimoResponseDTO>> getAllEmprestimos() {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getAllEmprestimos());
    }

    @GetMapping("/{idLivro}/{idPessoa}")
    @Operation(summary = "Busca um empréstimo pelo ID do livro e da pessoa")
    public ResponseEntity<EmprestimoResponseDTO> getEmprestimoById(@PathVariable Long idLivro, @PathVariable Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se USER, só pode acessar empréstimo próprio
        if (!usuario.getRole().name().equals("ADMIN") && !usuario.getIdPessoa().equals(idPessoa)) {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado ao empréstimo consultado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getEmprestimoById(idLivro, idPessoa));
    }

    @PostMapping
    @Operation(summary = "Realiza o empréstimo de um livro para uma pessoa")
    public ResponseEntity<EmprestimoResponseDTO> emprestarLivro(@RequestBody @Valid EmprestimoRequestDTO emprestimoRequestDTO, org.springframework.security.core.Authentication authentication) {
        // Pega o usuário autenticado
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Força o idPessoa do DTO para o usuário autenticado
        emprestimoRequestDTO.setIdPessoa(usuario.getIdPessoa());
        // Regra: limitar a 3 empréstimos ativos por usuário
        List<EmprestimoResponseDTO> ativos = emprestimoService.getEmprestimosAtivos();
        long qtdAtivos = ativos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).count();
        if (qtdAtivos >= 3) {
            throw new br.com.emakers.biblioteca_api.exception.general.BusinessRuleException("Limite de 3 empréstimos ativos atingido");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.createEmprestimo(emprestimoRequestDTO));
    }

    @PutMapping("/{idLivro}")
    @Operation(summary = "Devolve um livro emprestado")
    public ResponseEntity<EmprestimoResponseDTO> devolverLivro(@PathVariable Long idLivro, Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se ADMIN, pode devolver para qualquer pessoa; se USER, só para si mesmo
        Long pessoaParaDevolver = usuario.getRole().name().equals("ADMIN") && idPessoa != null ? idPessoa : usuario.getIdPessoa();
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.updateEmprestimo(idLivro, pessoaParaDevolver));
    }

    @DeleteMapping("/{idLivro}")
    @Operation(summary = "Remove um empréstimo de livro")
    public ResponseEntity<Void> deleteEmprestimo(@PathVariable Long idLivro, Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se ADMIN, pode excluir para qualquer pessoa; se USER, só para si mesmo
        Long pessoaParaExcluir = usuario.getRole().name().equals("ADMIN") && idPessoa != null ? idPessoa : usuario.getIdPessoa();
        emprestimoService.deleteEmprestimo(idLivro, pessoaParaExcluir);
        return ResponseEntity.noContent().build();
    }

    // Histórico de empréstimos por pessoa
    @GetMapping("/historico/pessoa/{idPessoa}")
    @Operation(summary = "Consulta o histórico de empréstimos de uma pessoa")
    public ResponseEntity<List<EmprestimoResponseDTO>> getHistoricoPorPessoa(@PathVariable Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se USER, só pode acessar histórico próprio
        if (!usuario.getRole().name().equals("ADMIN") && !usuario.getIdPessoa().equals(idPessoa)) {
            throw new AccessDeniedException("Acesso negado ao histórico da pessoa informada");
        }
        return ResponseEntity.ok(emprestimoService.getHistoricoEmprestimosPorPessoa(idPessoa));
    }

    // Histórico de empréstimos por livro
    @GetMapping("/historico/livro/{idLivro}")
    @Operation(summary = "Consulta o histórico de empréstimos de um livro")
    public ResponseEntity<List<EmprestimoResponseDTO>> getHistoricoPorLivro(@PathVariable Long idLivro, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        if (usuario.getRole().name().equals("ADMIN")) {
            return ResponseEntity.ok(emprestimoService.getHistoricoEmprestimosPorLivro(idLivro));
        } else {
            // Filtra só os empréstimos do usuário
            List<EmprestimoResponseDTO> todos = emprestimoService.getHistoricoEmprestimosPorLivro(idLivro);
            List<EmprestimoResponseDTO> meus = todos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).collect(java.util.stream.Collectors.toList());
            if (meus.isEmpty()) {
                throw new org.springframework.security.access.AccessDeniedException("Histórico não pertence ao usuário");
            }
            return ResponseEntity.ok(meus);
        }
    }

    // Empréstimos atrasados
    @GetMapping("/atrasados")
    @Operation(summary = "Lista todos os empréstimos atrasados")
    public ResponseEntity<List<EmprestimoResponseDTO>> getEmprestimosAtrasados(org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        if (usuario.getRole().name().equals("ADMIN")) {
            return ResponseEntity.ok(emprestimoService.getEmprestimosAtrasados());
        } else {
            List<EmprestimoResponseDTO> todos = emprestimoService.getEmprestimosAtrasados();
            List<EmprestimoResponseDTO> meus = todos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).collect(java.util.stream.Collectors.toList());
            if (meus.isEmpty()) {
                throw new org.springframework.security.access.AccessDeniedException("Nenhum empréstimo atrasado para o usuário");
            }
            return ResponseEntity.ok(meus);
        }
    }

    // Empréstimos ativos
    @GetMapping("/ativos")
    @Operation(summary = "Lista todos os empréstimos ativos")
    public ResponseEntity<List<EmprestimoResponseDTO>> getEmprestimosAtivos(org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        if (usuario.getRole().name().equals("ADMIN")) {
            return ResponseEntity.ok(emprestimoService.getEmprestimosAtivos());
        } else {
            List<EmprestimoResponseDTO> todos = emprestimoService.getEmprestimosAtivos();
            List<EmprestimoResponseDTO> meus = todos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).collect(java.util.stream.Collectors.toList());
            if (meus.isEmpty()) {
                throw new org.springframework.security.access.AccessDeniedException("Nenhum empréstimo ativo para o usuário");
            }
            return ResponseEntity.ok(meus);
        }
    }
}
