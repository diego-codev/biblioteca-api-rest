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

    @GetMapping(value = "/all")
    public ResponseEntity<List<EmprestimoResponseDTO>> getAllEmprestimos() {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getAllEmprestimos());
    }

    @GetMapping(value = "/{idLivro}/{idPessoa}")
    public ResponseEntity<EmprestimoResponseDTO> getEmprestimoById(@PathVariable Long idLivro, @PathVariable Long idPessoa) {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getEmprestimoById(idLivro, idPessoa));
    }


    @PostMapping(value = "/emprestar")
    public ResponseEntity<EmprestimoResponseDTO> emprestarLivro(@RequestBody EmprestimoRequestDTO emprestimoRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.createEmprestimo(emprestimoRequestDTO));
    }

    @PutMapping(value = "/devolucao/{idLivro}/{idPessoa}")
    public ResponseEntity<EmprestimoResponseDTO> devolverLivro(@PathVariable Long idLivro, @PathVariable Long idPessoa) {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.updateEmprestimo(idLivro, idPessoa));
    }

    @DeleteMapping(value = "/delete/{idLivro}/{idPessoa}")
    public ResponseEntity<String> deleteEmprestimo(@PathVariable Long idLivro, @PathVariable Long idPessoa) {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.deleteEmprestimo(idLivro, idPessoa));
    }

    // Histórico de empréstimos por pessoa
    @GetMapping("/historico/pessoa/{idPessoa}")
    public ResponseEntity<List<EmprestimoResponseDTO>> getHistoricoPorPessoa(@PathVariable Long idPessoa) {
        return ResponseEntity.ok(emprestimoService.getHistoricoEmprestimosPorPessoa(idPessoa));
    }

    // Histórico de empréstimos por livro
    @GetMapping("/historico/livro/{idLivro}")
    public ResponseEntity<List<EmprestimoResponseDTO>> getHistoricoPorLivro(@PathVariable Long idLivro) {
        return ResponseEntity.ok(emprestimoService.getHistoricoEmprestimosPorLivro(idLivro));
    }

    // Empréstimos atrasados
    @GetMapping("/atrasados")
    public ResponseEntity<List<EmprestimoResponseDTO>> getEmprestimosAtrasados() {
        return ResponseEntity.ok(emprestimoService.getEmprestimosAtrasados());
    }

    // Empréstimos ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<EmprestimoResponseDTO>> getEmprestimosAtivos() {
        return ResponseEntity.ok(emprestimoService.getEmprestimosAtivos());
    }

}
