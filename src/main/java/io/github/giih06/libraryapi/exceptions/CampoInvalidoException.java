package io.github.giih06.libraryapi.exceptions;

import lombok.Getter;

/**
 * Exceção lançada quando um campo específico da requisição está inválido,
 * permitindo informar o nome do campo e uma mensagem de erro personalizada.
 *
 * Utilizada geralmente para validações manuais fora do contexto do Bean Validation.
 */
public class CampoInvalidoException extends RuntimeException {

    @Getter
    private String campo;

    public CampoInvalidoException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
    }

}
