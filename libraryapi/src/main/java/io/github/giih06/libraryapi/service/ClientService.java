package io.github.giih06.libraryapi.service;

import io.github.giih06.libraryapi.model.Client;
import io.github.giih06.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Client salvar(Client client) {
        var senhaCriptografada = passwordEncoder.encode(client.getClientSecret());
        client.setClientSecret(senhaCriptografada);
        return repository.save(client);
    }

    public Client obterPorClientId(String clientId) {
        return repository.findByClientId(clientId);
    }
}
