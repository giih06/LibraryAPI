package io.github.giih06.libraryapi.repository;

import io.github.giih06.libraryapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repositório para operações de persistência com a entidade Client.
 */
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByClientId(String clientId);
}
