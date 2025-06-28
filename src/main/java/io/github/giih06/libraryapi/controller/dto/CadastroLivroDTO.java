package io.github.giih06.libraryapi.controller.dto;

import io.github.giih06.libraryapi.model.GeneroLivro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO responsável pelo recebimento dos dados de cadastro de um livro.
 *
 * Contém validações para garantir integridade dos dados e é utilizado
 * principalmente em operações de criação via API REST.
 */
@Schema(name = "Livro")
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
