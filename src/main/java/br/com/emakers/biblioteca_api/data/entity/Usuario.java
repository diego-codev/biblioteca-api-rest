// ...existing code...
package br.com.emakers.biblioteca_api.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements org.springframework.security.core.userdetails.UserDetails {
    public Usuario(String email, String senha, UserRole role) {
        this.email = email;
        this.senha = senha;
        this.role = role;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return java.util.List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority(UserRole.ADMIN.getRole()),
                new org.springframework.security.core.authority.SimpleGrantedAuthority(UserRole.USER.getRole())
            );
        }
        return java.util.List.of(
            new org.springframework.security.core.authority.SimpleGrantedAuthority(UserRole.USER.getRole())
        );
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
