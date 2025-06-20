package io.github.giih06.libraryapi.controller.dto;

import io.github.giih06.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

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
