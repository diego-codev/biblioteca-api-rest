package br.com.emakers.biblioteca_api.service;

import br.com.emakers.biblioteca_api.client.ViaCepClient;
import br.com.emakers.biblioteca_api.data.entity.Pessoa;
import br.com.emakers.biblioteca_api.data.entity.UserRole;
import br.com.emakers.biblioteca_api.data.dto.request.RegisterDTO;
import br.com.emakers.biblioteca_api.data.dto.response.ViaCepResponseDTO;
import br.com.emakers.biblioteca_api.exception.general.BusinessRuleException;
import br.com.emakers.biblioteca_api.repository.PessoaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PessoaRegistrationFacade {
    private final PessoaRepository pessoaRepository;
    private final ViaCepClient viaCepClient;
    private final PasswordEncoder passwordEncoder;

    public PessoaRegistrationFacade(PessoaRepository pessoaRepository,
                                    ViaCepClient viaCepClient,
                                    PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.viaCepClient = viaCepClient;
        this.passwordEncoder = passwordEncoder;
    }

    public Pessoa registrarNovaPessoa(RegisterDTO data) {
        if (pessoaRepository.findByEmailIgnoreCase(data.email()).isPresent()) {
            throw new BusinessRuleException("Email já cadastrado");
        }
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(data.nome());
        pessoa.setEmail(data.email().trim());
        pessoa.setCpf(apenasDigitos(data.cpf()));
        pessoa.setCep(apenasDigitos(data.cep()));
        pessoa.setSenha(passwordEncoder.encode(data.password()));
        pessoa.setRole(data.role() != null ? data.role() : UserRole.USER);
        preencherEnderecoPorCep(pessoa, pessoa.getCep());
        return pessoaRepository.save(pessoa);
    }

    private void preencherEnderecoPorCep(Pessoa pessoa, String cep) {
        ViaCepResponseDTO endereco = viaCepClient.buscarEnderecoPorCep(cep);
        if (endereco == null || endereco.isInvalido() || (endereco.getLogradouro() == null && endereco.getLocalidade() == null)) {
            throw new BusinessRuleException("CEP inválido ou não encontrado");
        }
        pessoa.setLogradouro(endereco.getLogradouro());
        pessoa.setBairro(endereco.getBairro());
        pessoa.setLocalidade(endereco.getLocalidade());
        pessoa.setUf(endereco.getUf());
    }

    private String apenasDigitos(String v) { return v.replaceAll("\\D", ""); }
}
