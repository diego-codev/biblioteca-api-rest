package br.com.emakers.biblioteca_api.client;

import br.com.emakers.biblioteca_api.data.dto.response.ViaCepResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public ViaCepResponseDTO buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, ViaCepResponseDTO.class);
    }
}
