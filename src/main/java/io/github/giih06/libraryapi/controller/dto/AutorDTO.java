package io.github.giih06.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) para a entidade Autor.
 *
 * Utilizado para entrada e saída de dados da API relacionados a autores,
 * com validações aplicadas nos campos.
 */
@Schema(name = "Autor")  // Utilizado pelo Swagger/OpenAPI para gerar documentação do schema
public record AutorDTO(
        UUID id,
        @NotBlank(message = "Nome é obrigatório!")
        @Size(min = 2, max = 100, message = "Campo fora do tamanho permitido")
        String nome,
        @NotNull(message = "Data de nascimento é obrigatório!")
        @Past(message = "Este campo não permite uma data futura")
        LocalDate dataNascimento,
        @NotBlank(message = "Nacionalidade é obrigatório!")
        @Size(min = 2, max = 50, message = "Campo fora do tamanho permitido")
        String nacionalidade) {
}
