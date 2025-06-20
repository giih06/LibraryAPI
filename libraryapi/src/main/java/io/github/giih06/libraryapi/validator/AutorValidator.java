package io.github.giih06.libraryapi.validator;

import io.github.giih06.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private final AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor) {
        if(existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado no banco de dados");
        }
    }

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
