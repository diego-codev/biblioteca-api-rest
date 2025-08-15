package br.com.emakers.biblioteca_api.infra.config;

import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;
import io.swagger.v3.oas.models.responses.ApiResponse;

/**
 * Adiciona respostas padrão essenciais para todos os endpoints.
 * Regras:
 *  GET: 200 (e 404 se path contém '{').
 *  POST: 201, 400.
 *  PUT: 200, 400, 404.
 *  DELETE: 204, 404.
 *  Globais básicos: 401, 403, 500.
 *  NÃO adiciona 409/422/500 automaticamente; só declarar onde realmente necessário.
 */
@Configuration
public class OpenApiGlobalResponsesConfig {

    @Bean
    public OpenApiCustomizer globalResponsesCustomiser() {
        return openApi -> {
            if (openApi.getPaths() == null) return;
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(op -> {
                ApiResponses responses = op.getResponses();
                String path = openApi.getPaths().entrySet().stream()
                    .filter(e -> e.getValue() == pathItem).map(java.util.Map.Entry::getKey).findFirst().orElse("");
                io.swagger.v3.oas.models.PathItem.HttpMethod httpMethod = pathItem.readOperationsMap().entrySet().stream()
                    .filter(en -> en.getValue() == op).map(java.util.Map.Entry::getKey).findFirst().orElse(null);

                // Globais básicos
                addIfMissing(responses, "401", "Não autenticado (token ausente ou inválido)");
                addIfMissing(responses, "403", "Acesso negado (permissões insuficientes)");
                addIfMissing(responses, "500", "Erro interno inesperado");

                if (httpMethod == null) return;
                switch (httpMethod) {
                    case GET -> {
                        addIfMissing(responses, "200", "Sucesso");
                        if (path.contains("{")) addIfMissing(responses, "404", "Recurso não encontrado");
                    }
                    case POST -> {
                        addIfMissing(responses, "201", "Recurso criado com sucesso");
                        addIfMissing(responses, "400", "Requisição inválida (dados incorretos)");
                    }
                    case PUT -> {
                        addIfMissing(responses, "200", "Recurso atualizado com sucesso");
                        addIfMissing(responses, "400", "Requisição inválida (dados incorretos)");
                        addIfMissing(responses, "404", "Recurso não encontrado");
                    }
                    case DELETE -> {
                        addIfMissing(responses, "204", "Recurso removido (sem conteúdo)");
                        addIfMissing(responses, "404", "Recurso não encontrado");
                    }
                    default -> {}
                }
                // Remove qualquer conteúdo (media type / schema) para exibir só código + descrição
                responses.forEach((code, apiResp) -> apiResp.setContent(null));
            }));
        };
    }

    private void addIfMissing(ApiResponses responses, String code, String description) {
        if (responses.containsKey(code)) return;
    ApiResponse apiResponse = new ApiResponse().description(description);
        responses.addApiResponse(code, apiResponse);
    }
}
