package io.github.giih06.libraryapi.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um usuário do sistema.
 *
 * Armazena credenciais de acesso e os papéis (roles) associados ao usuário.
 * Utiliza UUID como identificador e tipo personalizado para armazenar a lista de roles no banco.
 */
@Entity
@Table
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String login;

    @Column
    private String senha;

    @Column
    private String email;

    // faz a conversão de list para array
    @Type(ListArrayType.class)
    @Column(name = "roles", columnDefinition = "varchar[]")
    private List<String> roles;
}
