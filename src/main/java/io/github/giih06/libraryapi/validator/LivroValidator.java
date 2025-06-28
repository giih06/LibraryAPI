package io.github.giih06.libraryapi.validator;

import io.github.giih06.libraryapi.exceptions.CampoInvalidoException;
import io.github.giih06.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Componente responsável pela validação da entidade Livro.
 * Garante integridade dos dados antes de persistir no banco de dados.
 */
@Component
@RequiredArgsConstructor
public class LivroValidator {

    // Ano a partir do qual o preço do livro se torna obrigatório
    private static final int ANO_EXIGE_PRECO = 2020;

    // Repositório para acesso aos dados de livros
    private final LivroRepository repository;

    /**
     * Realiza validações de negócio sobre a entidade Livro.
     * Verifica duplicidade de ISBN e obrigatoriedade de preço.
     *
     * @param livro livro a ser validado
     * @throws RegistroDuplicadoException se o ISBN já estiver cadastrado
     * @throws CampoInvalidoException se o preço for obrigatório e estiver ausente
     */
    public void validar(Livro livro) {
        if(existeLivroComIsbn(livro)) {
            throw new RegistroDuplicadoException("ISBN já cadastrado no banco de dados");
        }

        if(isPrecoObrigatorioAndNulo(livro)) {
            throw new CampoInvalidoException("preço", "Para livros com ano de publicação a partir de 2020, o preço é obrigatório!");
        }
    }

    /**
     * Verifica se o preço é obrigatório (para livros publicados a partir de 2020)
     * e se ele está ausente (null).
     *
     * @param livro livro sendo validado
     * @return true se o preço for obrigatório e estiver nulo
     */
    private boolean isPrecoObrigatorioAndNulo(Livro livro) {
        return livro.getPreco() == null                    // comparação correta
                && livro.getDataPublicacao().getYear() >= ANO_EXIGE_PRECO;
    }

    /**
     * Verifica se já existe outro livro com o mesmo ISBN cadastrado.
     * Considera dois cenários:
     * - Inserção de novo livro: qualquer ISBN existente é duplicado.
     * - Edição de livro existente: só é duplicado se for de outro livro (IDs diferentes).
     *
     * @param livro livro sendo validado
     * @return true se o ISBN estiver duplicado
     */
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
