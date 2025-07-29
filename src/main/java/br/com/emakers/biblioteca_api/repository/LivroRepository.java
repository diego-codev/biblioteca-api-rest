package br.com.emakers.biblioteca_api.repository;

import br.com.emakers.biblioteca_api.data.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
}
