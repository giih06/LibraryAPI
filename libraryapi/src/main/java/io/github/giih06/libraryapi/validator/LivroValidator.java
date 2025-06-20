package io.github.giih06.libraryapi.validator;

import io.github.giih06.libraryapi.exceptions.CampoInvalidoException;
import io.github.giih06.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private static final int ANO_EXIGE_PRECO = 2020;

    private final LivroRepository repository;

    public void validar(Livro livro) {
        if(existeLivroComIsbn(livro)) {
            throw new RegistroDuplicadoException("ISBN já cadastrado no banco de dados");
        }

        if(isPrecoObrigatorioAndNulo(livro)) {
            throw new CampoInvalidoException("preço", "Para livros com ano de publicação a partir de 2020, o preço é obrigatório!");
        }
    }

    private boolean isPrecoObrigatorioAndNulo(Livro livro) {
        return livro.getPreco() == null                    // comparação correta
                && livro.getDataPublicacao().getYear() >= ANO_EXIGE_PRECO;
    }

    private boolean existeLivroComIsbn(Livro livro) {
        Optional<Livro> livroEncontrado = repository.findByIsbn(livro.getIsbn());

        // CENÁRIO 1: Inserção de novo livro (ID ainda não existe)
        if (livro.getId() == null) {
            return livroEncontrado.isPresent();
        }

        // CENÁRIO 2: Edição de livro existente
        // Se encontrou outro livro e o ID é diferente, trata-se de duplicidade
        return livroEncontrado
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));
    }
}
