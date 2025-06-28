package io.github.giih06.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Entidade que representa um cliente OAuth2 registrado no sistema.
 *
 * Armazena informações necessárias para autenticação e autorização de clientes,
 * como client_id, client_secret, URI de redirecionamento e escopos permitidos.
 */
@Entity
@Table
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "redirect_uri")
    private String redirectURI;

    @Column(name = "scope")
    private String scope;
}
