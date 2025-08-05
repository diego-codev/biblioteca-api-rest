package br.com.emakers.biblioteca_api.repository;

import br.com.emakers.biblioteca_api.data.entity.Usuario;
import br.com.emakers.biblioteca_api.data.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    // Para autenticação via UserDetailsService
    UserDetails findByEmailIgnoreCase(String email);
    // Para buscar por role, se necessário
    Optional<Usuario> findByRole(UserRole role);
}
