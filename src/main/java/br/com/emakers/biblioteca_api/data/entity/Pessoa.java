package br.com.emakers.biblioteca_api.data.entity;

import br.com.emakers.biblioteca_api.data.dto.request.PessoaRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPessoa;
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;
    
    // Campos de endereço para consumo da API externa de CEP
    @Column(name = "logradouro", length = 100)
    private String logradouro;

    @Column(name = "bairro", length = 60)
    private String bairro;

    @Column(name = "localidade", length = 60)
    private String localidade;

    @Column(name = "uf", length = 2)
    private String uf;

    @Builder
    public Pessoa(PessoaRequestDTO dto) {
        this.nome = dto.getNome();
        this.email = dto.getEmail();
        this.cep = dto.getCep();
        this.cpf = dto.getCpf();
        this.senha = dto.getSenha();
        // Os campos de endereço serão preenchidos no service
    }
}
