package br.com.emakers.biblioteca_api.controller;

import br.com.emakers.biblioteca_api.repository.UsuarioRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.emakers.biblioteca_api.data.dto.request.AuthenticationDTO;
import br.com.emakers.biblioteca_api.data.dto.request.RegisterDTO;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (usuarioRepository.findByEmail(data.login()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(data.password());
        var newUser = new br.com.emakers.biblioteca_api.data.entity.Usuario(data.login(), encryptedPassword, data.role());
        usuarioRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().build();
    }
}
