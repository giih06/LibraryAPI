package io.github.giih06.libraryapi.service;

import io.github.giih06.libraryapi.model.Client;
import io.github.giih06.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por operações relacionadas à entidade Client (cliente OAuth2).
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    // Repositório responsável pela persistência da entidade Client
    private final ClientRepository repository;

    // Componente para codificação segura de senhas (secret do client)
    private final PasswordEncoder passwordEncoder;

    /**
     * Salva um novo client no banco de dados.
     * Antes de persistir, a senha (client secret) é criptografada.
     *
     * @param client objeto Client a ser salvo
     * @return o client persistido com o secret criptografado
     */
    public Client salvar(Client client) {
        // Criptografa o client secret antes de persistir
        var senhaCriptografada = passwordEncoder.encode(client.getClientSecret());
        client.setClientSecret(senhaCriptografada);

        return repository.save(client);
    }

    /**
     * Busca um client pelo seu clientId.
     *
     * @param clientId identificador único do client (usado na autenticação OAuth2)
     * @return o client correspondente, ou null se não encontrado
     */
    public Client obterPorClientId(String clientId) {
        return repository.findByClientId(clientId);
    }
}
