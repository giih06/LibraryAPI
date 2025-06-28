package io.github.giih06.libraryapi.controller.dto;

/**
 * Representa um erro específico em um campo de entrada da requisição.
 *
 * Usado em respostas de validação para detalhar quais campos estão inválidos
 * e qual a mensagem de erro associada a cada um.
 *
 * Exemplo de uso:
 * {
 *   "campo": "titulo",
 *   "erro": "Título é obrigatório"
 * }
 *
 * @param campo nome do campo que apresentou erro
 * @param erro  mensagem descritiva do erro
 */
public record ErroCampo(String campo, String erro) {
}
