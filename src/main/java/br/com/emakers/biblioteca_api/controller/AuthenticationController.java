package br.com.emakers.biblioteca_api.controller;

import br.com.emakers.biblioteca_api.exception.general.RestErrorMessage;
import br.com.emakers.biblioteca_api.data.entity.Pessoa;
import br.com.emakers.biblioteca_api.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import br.com.emakers.biblioteca_api.data.dto.request.AuthenticationDTO;
import br.com.emakers.biblioteca_api.data.dto.request.RegisterDTO;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Registro (Pessoa) e login (JWT)")
public class AuthenticationController {
    @Autowired
    private br.com.emakers.biblioteca_api.infra.security.TokenService tokenService;
    @Autowired
    private PessoaService pessoaService;

    @PostMapping("/register")
    @Operation(summary = "Cadastra uma nova pessoa completa (valida CEP e preenche endereço)")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        try {
            Pessoa pessoa = pessoaService.registrar(data);
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(br.com.emakers.biblioteca_api.data.dto.response.RegisterResponseDTO.from(
                        pessoa.getIdPessoa(),
                        pessoa.getEmail(),
                        pessoa.getNome(),
                        pessoa.getRole(),
                        pessoa.getCep(),
                        pessoa.getLogradouro(),
                        pessoa.getBairro(),
                        pessoa.getLocalidade(),
                        pessoa.getUf()
                    ));
        } catch (br.com.emakers.biblioteca_api.exception.general.BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RestErrorMessage.of(HttpStatus.BAD_REQUEST, e.getMessage(), "/auth/register"));
        }
    }
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "Realiza o login e retorna o token JWT")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
    var token = tokenService.generateToken((Pessoa) auth.getPrincipal());
        return ResponseEntity.ok(new br.com.emakers.biblioteca_api.data.dto.response.LoginResponseDTO(token));
    }
}
