package br.com.emakers.biblioteca_api.repository;

import br.com.emakers.biblioteca_api.data.entity.SolicitacaoEmprestimoExterno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoEmprestimoExternoRepository extends JpaRepository<SolicitacaoEmprestimoExterno, Long> {
}
