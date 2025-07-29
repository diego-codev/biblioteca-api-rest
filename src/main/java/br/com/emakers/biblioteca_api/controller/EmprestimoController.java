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

    @PostMapping(value = "/create")
    public ResponseEntity<EmprestimoResponseDTO> createEmprestimo(@RequestBody EmprestimoRequestDTO emprestimoRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.createEmprestimo(emprestimoRequestDTO));
    }

    @PutMapping(value = "/update/{idLivro}/{idPessoa}")
    public ResponseEntity<EmprestimoResponseDTO> updateEmprestimo(@PathVariable Long idLivro, @PathVariable Long idPessoa, @RequestBody EmprestimoRequestDTO emprestimoRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.updateEmprestimo(idLivro, idPessoa, emprestimoRequestDTO));
    }

    @DeleteMapping(value = "/delete/{idLivro}/{idPessoa}")
    public ResponseEntity<String> deleteEmprestimo(@PathVariable Long idLivro, @PathVariable Long idPessoa) {
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.deleteEmprestimo(idLivro, idPessoa));
    }
}
