package io.github.giih06.libraryapi.validator;

import io.github.giih06.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Componente responsável por validar regras específicas da entidade Autor
 * antes de sua persistência ou atualização no banco de dados.
 */
@Component
public class AutorValidator {

    private final AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    /**
     * Valida se o autor já está cadastrado no banco.
     * Se houver conflito (mesmo nome, data de nascimento e nacionalidade), lança exceção.
     *
     * @param autor autor a ser validado
     * @throws RegistroDuplicadoException se o autor já existir no banco com os mesmos dados-chave
     */
    public void validar(Autor autor) {
        if(existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado no banco de dados");
        }
    }

    /**
     * Verifica se já existe um autor com os mesmos dados (nome, data de nascimento, nacionalidade).
     * Considera dois casos:
     * - Cadastro novo: impede duplicação completa
     * - Atualização: impede duplicação com IDs diferentes
     *
     * @param autor autor sendo validado
     * @return true se existir outro autor com os mesmos dados relevantes
     */
    private boolean existeAutorCadastrado(Autor autor) {
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade());

        // cadastrando um novo autor
        if(autor.getId() == null) {
            return autorEncontrado.isPresent();
        }

        // atualização: conflito se existirem dois registros com IDs diferentes
        return autorEncontrado
                .filter(value -> !autor.getId().equals(value.getId()))
                .isPresent();

    }
}
