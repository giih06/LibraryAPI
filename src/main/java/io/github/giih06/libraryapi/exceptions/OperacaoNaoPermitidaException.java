package io.github.giih06.libraryapi.exceptions;

/**
 * Exceção lançada quando o usuário tenta realizar uma operação inválida
 * ou proibida pelas regras de negócio da aplicação.
 *
 * Exemplo: tentar alterar um registro bloqueado ou remover um item em uso.
 */
public class OperacaoNaoPermitidaException extends RuntimeException {
    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
