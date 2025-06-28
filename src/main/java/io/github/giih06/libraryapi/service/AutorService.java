package io.github.giih06.libraryapi.service;

import io.github.giih06.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.repository.AutorRepository;
import io.github.giih06.libraryapi.repository.LivroRepository;
import io.github.giih06.libraryapi.securty.SecurityService;
import io.github.giih06.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pelas regras de negócio relacionadas à entidade Autor.
 */
@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;
    private final LivroRepository livroRepository;
    private final AutorValidator validator;
    private final SecurityService securityService;

    /**
     * Salva um novo autor após validar seus dados e associá-lo ao usuário logado.
     *
     * @param autor autor a ser salvo
     * @return autor salvo com ID gerado
     */
    public Autor salvar(Autor autor) {
        validator.validar(autor);
        Usuario user = securityService.obterUsuarioLogado();
        autor.setIdUsuario(user.getId());
        return repository.save(autor);
    }

    /**
     * Atualiza um autor existente no banco de dados.
     * Lança exceção se o autor ainda não tiver um ID (não persistido).
     *
     * @param autor autor com dados atualizados
     */
    public void atualizar(Autor autor) {
        if(autor.getId() == null) {
            throw new IllegalArgumentException("O autor precisa estar cadastrado para ser atualizado");
        }
        repository.save(autor);
    }

    /**
     * Busca um autor pelo ID.
     *
     * @param id identificador único do autor
     * @return Optional com o autor encontrado (ou vazio)
     */
    public Optional<Autor> obterPorId(UUID id) {
        return repository.findById(id);
    }

    /**
     * Remove um autor do banco, desde que ele não possua livros associados.
     *
     * @param autor autor a ser removido
     */
    public void deletar(Autor autor) {
        if(possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException("Autor não pode ser deletado pois possui livro(s) cadastrado(s)");
        }
        repository.delete(autor);
    }

    /**
     * Verifica se o autor possui livros cadastrados.
     *
     * @param autor autor a ser verificado
     * @return true se houver livros associados, false caso contrário
     */
    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }

    /* Forma não eficiente de aplicação de pesquisa
    public List<Autor> pesquisar(String nome, String nacionalidade) {
        if(nome != null && nacionalidade != null) {
            return repository.findByNomeAndNacionalidade(nome, nacionalidade);
        }

        if (nome != null) {
            return repository.findByNome(nome);
        }

        if (nacionalidade != null) {
            return repository.findByNacionalidade(nacionalidade);
        }

        return repository.findAll(); // não passou parâmetros
    } */

    /**
     * Realiza uma busca dinâmica por nome e nacionalidade usando Spring Data Example.
     * Ignora campos nulos e realiza busca "contendo" (parcial) com case-insensitive.
     *
     * @param nome nome parcial ou completo do autor
     * @param nacionalidade nacionalidade parcial ou completa
     * @return lista de autores que correspondem aos critérios
     */
    public List<Autor> pesquisaByExample(String nome, String nacionalidade) {
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "dataNascimento", "dataCadastro") // ignora campos irrelevantes para busca
                .withIgnoreCase() // ignora diferenciação entre maiúsculas/minúsculas
                .withIgnoreNullValues() // ignora campos nulos
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // busca por "contém"
        Example<Autor> autorExample = Example.of(autor, matcher);
        return repository.findAll(autorExample);
    }
}
