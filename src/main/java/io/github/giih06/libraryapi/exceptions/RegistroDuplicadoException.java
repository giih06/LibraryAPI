package io.github.giih06.libraryapi.exceptions;

/**
 * Exceção lançada quando há tentativa de cadastrar ou atualizar um
 * recurso com um valor que já existe no sistema e deve ser único.
 *
 * Exemplo: tentar registrar um usuário com e-mail já existente.
 */
public class RegistroDuplicadoException extends RuntimeException {

    public RegistroDuplicadoException(String message) {
        super(message);
    }
}
