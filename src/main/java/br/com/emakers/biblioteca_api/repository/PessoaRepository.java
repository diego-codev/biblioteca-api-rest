package br.com.emakers.biblioteca_api.repository;

import br.com.emakers.biblioteca_api.data.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
