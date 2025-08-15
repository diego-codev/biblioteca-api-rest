package br.com.emakers.biblioteca_api.controller;

import br.com.emakers.biblioteca_api.repository.UsuarioRepository;
import br.com.emakers.biblioteca_api.exception.general.RestErrorMessage;

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
@Tag(name = "Autenticação", description = "Registro e login de usuários")
public class AuthenticationController {
    @Autowired
    private br.com.emakers.biblioteca_api.infra.security.TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    @Operation(summary = "Realiza o cadastro de um novo usuário")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
    if (usuarioRepository.findByEmail(data.login()).isPresent()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(RestErrorMessage.of(HttpStatus.BAD_REQUEST, "Email já cadastrado", "/auth/register"));
    }
        String encryptedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(data.password());
        var newUser = new br.com.emakers.biblioteca_api.data.entity.Usuario(data.login(), encryptedPassword, data.role());
        usuarioRepository.save(newUser);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new br.com.emakers.biblioteca_api.data.dto.response.RegisterResponseDTO(newUser.getId(), newUser.getEmail(), newUser.getRole()));
    }
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "Realiza o login e retorna o token JWT")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((br.com.emakers.biblioteca_api.data.entity.Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new br.com.emakers.biblioteca_api.data.dto.response.LoginResponseDTO(token));
    }
}
