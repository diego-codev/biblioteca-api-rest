package br.com.emakers.biblioteca_api.repository;

import br.com.emakers.biblioteca_api.data.entity.Emprestimo;
import br.com.emakers.biblioteca_api.data.entity.EmprestimoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, EmprestimoId> {
}
