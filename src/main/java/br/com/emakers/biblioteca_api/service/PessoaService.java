package br.com.emakers.biblioteca_api.service;

import br.com.emakers.biblioteca_api.data.dto.request.PessoaRequestDTO;
import br.com.emakers.biblioteca_api.data.dto.response.PessoaResponseDTO;
import br.com.emakers.biblioteca_api.repository.PessoaRepository;
import br.com.emakers.biblioteca_api.data.entity.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

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
        pessoaRepository.save(pessoa);
        return new PessoaResponseDTO(pessoa);
    }


    public PessoaResponseDTO updatePessoa(Long idPessoa, PessoaRequestDTO pessoaRequestDTO) {
        Pessoa pessoa = getPessoaEntityById(idPessoa);
        pessoa.setNome(pessoaRequestDTO.getNome());
        pessoa.setEmail(pessoaRequestDTO.getEmail());
        pessoa.setCep(pessoaRequestDTO.getCep());
        pessoa.setCpf(pessoaRequestDTO.getCpf());
        pessoaRepository.save(pessoa);
        return new PessoaResponseDTO(pessoa);
    }


    public String deletePessoa(Long idPessoa) {
        Pessoa pessoa = getPessoaEntityById(idPessoa);
        pessoaRepository.delete(pessoa);
        return "Pessoa id: " + idPessoa + " deletada!";
    }

    private Pessoa getPessoaEntityById(Long idPessoa) {
        return pessoaRepository.findById(idPessoa)
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
    }
}
