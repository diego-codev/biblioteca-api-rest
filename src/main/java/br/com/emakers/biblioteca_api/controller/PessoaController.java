
package br.com.emakers.biblioteca_api.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import br.com.emakers.biblioteca_api.data.dto.request.PessoaRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.PessoaResponseDTO;
import br.com.emakers.biblioteca_api.service.PessoaService;
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
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
@Tag(name = "Pessoas", description = "Operações de CRUD de pessoas")
@SecurityRequirement(name = "bearerAuth")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;


    @Operation(summary = "Lista todas as pessoas cadastradas")
    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> getAllPessoas() {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.getAllPessoas());
    }

    @Operation(summary = "Busca uma pessoa pelo ID")
    @GetMapping("/{idPessoa}")
    public ResponseEntity<PessoaResponseDTO> getPessoaById(@PathVariable Long idPessoa) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.getPessoaById(idPessoa));
    }

    @Operation(summary = "Cadastra uma nova pessoa")
    @PostMapping
    public ResponseEntity<PessoaResponseDTO> createPessoa(@RequestBody @Valid PessoaRequestDTO pessoaRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.createPessoa(pessoaRequestDTO));
    }

    @Operation(summary = "Atualiza os dados de uma pessoa pelo ID")
    @PutMapping("/{idPessoa}")
    public ResponseEntity<PessoaResponseDTO> updatePessoa(@PathVariable Long idPessoa, @RequestBody @Valid PessoaRequestDTO pessoaRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.updatePessoa(idPessoa, pessoaRequestDTO));
    }

    @Operation(summary = "Remove uma pessoa pelo ID")
    @DeleteMapping("/{idPessoa}")
    public ResponseEntity<Void> deletePessoa(@PathVariable Long idPessoa) {
        pessoaService.deletePessoa(idPessoa);
        return ResponseEntity.noContent().build();
    }
}
