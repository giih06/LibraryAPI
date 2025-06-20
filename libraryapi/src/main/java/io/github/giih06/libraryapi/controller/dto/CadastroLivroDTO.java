package io.github.giih06.libraryapi.controller.dto;

import io.github.giih06.libraryapi.model.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(
        @ISBN
        @NotBlank(message = "ISBN é obrigatório!")
        String isbn,
        @NotBlank(message = "título é obrigatório!")
        String titulo,
        @NotNull(message = "data_publicacao é obrigatório!")
        @Past(message = "Este campo não permite uma data futura")
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        @NotNull(message = "id_autor é obrigatório!")
        UUID idAutor) {
}
