package br.com.emakers.biblioteca_api.controller;
import br.com.emakers.biblioteca_api.data.dto.request.EmprestimoRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.EmprestimoResponseDTO;
import br.com.emakers.biblioteca_api.service.EmprestimoService;
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
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    public ResponseEntity<List<EmprestimoResponseDTO>> getAllEmprestimos() {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getAllEmprestimos());
    }

    @GetMapping("/{idLivro}/{idPessoa}")
    public ResponseEntity<EmprestimoResponseDTO> getEmprestimoById(@PathVariable Long idLivro, @PathVariable Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se USER, só pode acessar empréstimo próprio
        if (!usuario.getRole().name().equals("ADMIN") && !usuario.getIdPessoa().equals(idPessoa)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getEmprestimoById(idLivro, idPessoa));
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponseDTO> emprestarLivro(@RequestBody EmprestimoRequestDTO emprestimoRequestDTO, org.springframework.security.core.Authentication authentication) {
        // Pega o usuário autenticado
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Força o idPessoa do DTO para o usuário autenticado
        emprestimoRequestDTO.setIdPessoa(usuario.getIdPessoa());
        // Regra: limitar a 3 empréstimos ativos por usuário
        List<EmprestimoResponseDTO> ativos = emprestimoService.getEmprestimosAtivos();
        long qtdAtivos = ativos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).count();
        if (qtdAtivos >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.createEmprestimo(emprestimoRequestDTO));
    }

    @PutMapping("/{idLivro}")
    public ResponseEntity<EmprestimoResponseDTO> devolverLivro(@PathVariable Long idLivro, Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se ADMIN, pode devolver para qualquer pessoa; se USER, só para si mesmo
        Long pessoaParaDevolver = usuario.getRole().name().equals("ADMIN") && idPessoa != null ? idPessoa : usuario.getIdPessoa();
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.updateEmprestimo(idLivro, pessoaParaDevolver));
    }

    @DeleteMapping("/{idLivro}")
    public ResponseEntity<String> deleteEmprestimo(@PathVariable Long idLivro, Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se ADMIN, pode excluir para qualquer pessoa; se USER, só para si mesmo
        Long pessoaParaExcluir = usuario.getRole().name().equals("ADMIN") && idPessoa != null ? idPessoa : usuario.getIdPessoa();
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.deleteEmprestimo(idLivro, pessoaParaExcluir));
    }

    // Histórico de empréstimos por pessoa
    @GetMapping("/historico/pessoa/{idPessoa}")
    public ResponseEntity<List<EmprestimoResponseDTO>> getHistoricoPorPessoa(@PathVariable Long idPessoa, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        // Se USER, só pode acessar histórico próprio
        if (!usuario.getRole().name().equals("ADMIN") && !usuario.getIdPessoa().equals(idPessoa)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(emprestimoService.getHistoricoEmprestimosPorPessoa(idPessoa));
    }

    // Histórico de empréstimos por livro
    @GetMapping("/historico/livro/{idLivro}")
    public ResponseEntity<List<EmprestimoResponseDTO>> getHistoricoPorLivro(@PathVariable Long idLivro, org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        if (usuario.getRole().name().equals("ADMIN")) {
            return ResponseEntity.ok(emprestimoService.getHistoricoEmprestimosPorLivro(idLivro));
        } else {
            // Filtra só os empréstimos do usuário
            List<EmprestimoResponseDTO> todos = emprestimoService.getHistoricoEmprestimosPorLivro(idLivro);
            List<EmprestimoResponseDTO> meus = todos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).collect(java.util.stream.Collectors.toList());
            if (meus.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.ok(meus);
        }
    }

    // Empréstimos atrasados
    @GetMapping("/atrasados")
    public ResponseEntity<List<EmprestimoResponseDTO>> getEmprestimosAtrasados(org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        if (usuario.getRole().name().equals("ADMIN")) {
            return ResponseEntity.ok(emprestimoService.getEmprestimosAtrasados());
        } else {
            List<EmprestimoResponseDTO> todos = emprestimoService.getEmprestimosAtrasados();
            List<EmprestimoResponseDTO> meus = todos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).collect(java.util.stream.Collectors.toList());
            if (meus.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.ok(meus);
        }
    }

    // Empréstimos ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<EmprestimoResponseDTO>> getEmprestimosAtivos(org.springframework.security.core.Authentication authentication) {
        br.com.emakers.biblioteca_api.data.entity.Usuario usuario = (br.com.emakers.biblioteca_api.data.entity.Usuario) authentication.getPrincipal();
        if (usuario.getRole().name().equals("ADMIN")) {
            return ResponseEntity.ok(emprestimoService.getEmprestimosAtivos());
        } else {
            List<EmprestimoResponseDTO> todos = emprestimoService.getEmprestimosAtivos();
            List<EmprestimoResponseDTO> meus = todos.stream().filter(e -> e.idPessoa().equals(usuario.getIdPessoa())).collect(java.util.stream.Collectors.toList());
            if (meus.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.ok(meus);
        }
    }
}
