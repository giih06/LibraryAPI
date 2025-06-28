package io.github.giih06.libraryapi.controller.dto;

import io.github.giih06.libraryapi.model.GeneroLivro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO utilizado para retorno de dados de um livro em operações de pesquisa/listagem.
 *
 * Representa a estrutura de saída da API contendo informações completas do livro,
 * incluindo o autor associado e metadados como ID, ISBN, gênero e preço.
 *
 * Este DTO é ideal para visualização em telas de listagem, consulta por ID ou busca por filtros.
 */
@Schema(name = "Livro")
public record ResultadoPesquisaLivroDTO(
        UUID id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        AutorDTO autor) {
}
