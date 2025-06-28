package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

/**
 * Implementação personalizada do RegisteredClientRepository.
 * Responsável por fornecer os dados dos clientes OAuth2 registrados no sistema.
 */
@Component
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    // Serviço que fornece dados dos clientes cadastrados na aplicação
    private final ClientService clientService;

    // Configurações de tokens (tempo de expiração, reutilização, etc.)
    private final TokenSettings tokenSettings;

    // Configurações do cliente OAuth2
    private final ClientSettings clientSettings;

    /**
     * Método não implementado nesta versão.
     * Pode ser usado futuramente para persistir um novo cliente OAuth2.
     */
    @Override
    public void save(RegisteredClient registeredClient) {
        // Intencionalmente não implementado
    }

    /**
     * Busca cliente OAuth2 pelo ID único.
     * Não implementado nesta versão.
     */
    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    /**
     * Busca e retorna um RegisteredClient (cliente OAuth2) com base no clientId fornecido.
     * Utiliza dados da entidade Client persistida em banco para construir um RegisteredClient compatível.
     */
    @Override
    public RegisteredClient findByClientId(String clientId) {
        var client = clientService.obterPorClientId(clientId); // Busca o client no banco

        if (client == null) {
            return null; // Se não encontrado, retorna null conforme contrato da interface
        }

        // Constrói um RegisteredClient com os dados retornados
        return RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectURI())
                .scope(client.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();
    }
}
