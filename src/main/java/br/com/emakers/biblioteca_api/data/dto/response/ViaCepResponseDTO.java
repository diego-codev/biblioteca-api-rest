package br.com.emakers.biblioteca_api.data.dto.response;

// DTO da resposta ViaCEP utilizado para preencher endereço em Pessoa
public class ViaCepResponseDTO {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade; // cidade
    private String uf;         // estado

    public String getCep() { return cep; }
    public String getLogradouro() { return logradouro; }
    public String getBairro() { return bairro; }
    public String getLocalidade() { return localidade; }
    public String getUf() { return uf; }
}
