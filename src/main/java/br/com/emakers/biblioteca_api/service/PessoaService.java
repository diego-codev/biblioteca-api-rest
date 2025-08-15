
package br.com.emakers.biblioteca_api.service;

import br.com.emakers.biblioteca_api.data.dto.request.PessoaRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.PessoaResponseDTO;
import br.com.emakers.biblioteca_api.repository.PessoaRepository;
import br.com.emakers.biblioteca_api.data.entity.Pessoa;
import br.com.emakers.biblioteca_api.exception.general.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.emakers.biblioteca_api.client.ViaCepClient;
import br.com.emakers.biblioteca_api.data.dto.response.ViaCepResponseDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ViaCepClient viaCepClient;

    public boolean existsById(Long idPessoa) {
        return pessoaRepository.existsById(idPessoa);
    }

    public List<PessoaResponseDTO> getAllPessoas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return pessoas.stream().map(PessoaResponseDTO::new).collect(Collectors.toList());
    }


    public PessoaResponseDTO getPessoaById(Long idPessoa) {
        Pessoa pessoa = getPessoaEntityById(idPessoa);
        return new PessoaResponseDTO(pessoa);
    }

    public PessoaResponseDTO createPessoa(PessoaRequestDTO pessoaRequestDTO) {
        Pessoa pessoa = new Pessoa(pessoaRequestDTO);
        normalizarCpfCep(pessoa);
        preencherEnderecoPorCep(pessoa, pessoa.getCep());
        pessoaRepository.save(pessoa);
        return new PessoaResponseDTO(pessoa);
    }


    public PessoaResponseDTO updatePessoa(Long idPessoa, PessoaRequestDTO pessoaRequestDTO) {
        Pessoa pessoa = getPessoaEntityById(idPessoa);
        String cepAnterior = pessoa.getCep();
        pessoa.setNome(pessoaRequestDTO.getNome());
        pessoa.setEmail(pessoaRequestDTO.getEmail());
        pessoa.setCep(pessoaRequestDTO.getCep());
        pessoa.setCpf(pessoaRequestDTO.getCpf());
        pessoa.setSenha(pessoaRequestDTO.getSenha());
        normalizarCpfCep(pessoa);
        // Recarrega endereço se CEP mudou
        if (!cepIgual(cepAnterior, pessoa.getCep())) {
            preencherEnderecoPorCep(pessoa, pessoa.getCep());
        }
        pessoaRepository.save(pessoa);
        return new PessoaResponseDTO(pessoa);
    }


    public void deletePessoa(Long idPessoa) {
        Pessoa pessoa = getPessoaEntityById(idPessoa);
        pessoaRepository.delete(pessoa);
    }

    private Pessoa getPessoaEntityById(Long idPessoa) {
        return pessoaRepository.findById(idPessoa)
            .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada: id=" + idPessoa));
    }

    private void preencherEnderecoPorCep(Pessoa pessoa, String cep) {
        ViaCepResponseDTO endereco = viaCepClient.buscarEnderecoPorCep(cep);
        if (endereco == null || endereco.isInvalido() || (endereco.getLogradouro() == null && endereco.getLocalidade() == null)) {
            throw new br.com.emakers.biblioteca_api.exception.general.BusinessRuleException("CEP inválido ou não encontrado");
        }
        pessoa.setLogradouro(endereco.getLogradouro());
        pessoa.setBairro(endereco.getBairro());
        pessoa.setLocalidade(endereco.getLocalidade());
        pessoa.setUf(endereco.getUf());
    }

    private void normalizarCpfCep(Pessoa pessoa) {
        if (pessoa.getCpf() != null) {
            pessoa.setCpf(apenasDigitos(pessoa.getCpf()));
        }
        if (pessoa.getCep() != null) {
            pessoa.setCep(apenasDigitos(pessoa.getCep()));
        }
    }

    private String apenasDigitos(String valor) {
        return valor.replaceAll("\\D", "");
    }

    private boolean cepIgual(String c1, String c2) {
        if (c1 == null || c2 == null) return c1 == c2; // ambos null
        return apenasDigitos(c1).equals(apenasDigitos(c2));
    }
}
