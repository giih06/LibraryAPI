package io.github.giih06.libraryapi.controller.common;

import io.github.giih06.libraryapi.controller.dto.ErroCampo;
import io.github.giih06.libraryapi.controller.dto.ErroResposta;
import io.github.giih06.libraryapi.exceptions.CampoInvalidoException;
import io.github.giih06.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.giih06.libraryapi.exceptions.RegistroDuplicadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe responsável por capturar e tratar exceções lançadas durante a execução dos endpoints.
 *
 * Centraliza o tratamento de erros para retornar respostas padronizadas à API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação de campos enviados no corpo da requisição (anotações @Valid).
     *
     * @param e exceção contendo os campos inválidos
     * @return resposta com status 422 (Unprocessable Entity) e detalhes dos erros de campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("Erro de validação: {} ", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> listaErros = fieldErrors
                .stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação", listaErros);
    }

    /**
     * Trata exceções lançadas ao tentar registrar dados duplicados.
     *
     * @param e exceção com mensagem de conflito
     * @return resposta com status 409 (Conflict)
     */
    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicadoException(RegistroDuplicadoException e) {
        return ErroResposta.conflito(e.getMessage());
    }

    /**
     * Trata exceções para operações não permitidas no contexto atual.
     *
     * @param e exceção com a mensagem de erro
     * @return resposta com status 400 (Bad Request)
     */
    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e) {
        return ErroResposta.respostaPadrao(e.getMessage());
    }

    /**
     * Trata erros de campo específicos lançados manualmente na aplicação.
     *
     * @param e exceção com o nome do campo e a mensagem de erro
     * @return resposta com status 422 (Unprocessable Entity)
     */
    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidoException(CampoInvalidoException e) {
        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                List.of(new ErroCampo(e.getCampo(), e.getMessage()))
        );
    }

    /**
     * Trata exceções de acesso negado quando o usuário está autenticado, mas não autorizado.
     *
     * @param e exceção de acesso negado
     * @return resposta com status 403 (Forbidden)
     */
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.FORBIDDEN) // forbidden é usando quando o usuário está autenticado mas não tem acesso
    public ErroResposta handleAccessDeniedException(AccessDeniedException e) {
        return new ErroResposta(HttpStatus.FORBIDDEN.value(), "Acesso Negado", List.of());
    }

    /**
     * Captura quaisquer exceções não tratadas explicitamente.
     *
     * @param e exceção genérica
     * @return resposta com status 500 (Internal Server Error)
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratados(RuntimeException e) {
        log.error("Erro inesperado: {} ", e);
        return new ErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado, entre em contato com a administração do sistema",
                List.of());
    }

}
