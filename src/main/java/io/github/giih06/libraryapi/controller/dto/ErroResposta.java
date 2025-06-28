package io.github.giih06.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Representa a estrutura padrão de resposta de erro retornada pela API.
 *
 * Inclui:
 * - Código de status HTTP
 * - Mensagem descritiva do erro
 * - Lista opcional com detalhes de erros por campo (caso haja validação)
 *
 * Usado principalmente nos handlers globais de exceções.
 *
 * Exemplo:
 * {
 *   "status": 422,
 *   "mensagem": "Erro de validação",
 *   "erros": [
 *     { "campo": "titulo", "erro": "Título é obrigatório" }
 *   ]
 * }
 *
 * @param status código de status HTTP (ex: 400, 409, 422)
 * @param mensagem mensagem descritiva do erro ocorrido
 * @param erros lista de erros por campo (opcional)
 */
public record ErroResposta(int status, String mensagem, List<ErroCampo> erros) {

    /**
     * Cria uma resposta padrão de erro com status 400 (Bad Request) e sem detalhes por campo.
     *
     * @param mensagem mensagem do erro
     * @return objeto ErroResposta com status 400
     */
    public static ErroResposta respostaPadrao(String mensagem) {
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }

    /**
     * Cria uma resposta de erro com status 409 (Conflict) e sem detalhes por campo.
     *
     * Usado em casos como tentativa de criar um registro já existente.
     *
     * @param mensagem mensagem do erro
     * @return objeto ErroResposta com status 409
     */
    public static ErroResposta conflito(String mensagem) {
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }
}
