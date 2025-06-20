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

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;
    private final LivroRepository livroRepository;
    private final AutorValidator validator;
    private final SecurityService securityService;

    public Autor salvar(Autor autor) {
        validator.validar(autor);
        Usuario user = securityService.obterUsuarioLogado();
        autor.setIdUsuario(user.getId());
        return repository.save(autor);
    }

    public void atualizar(Autor autor) {
        if(autor.getId() == null) {
            throw new IllegalArgumentException("O autor precisa estar cadastrado para ser atualizado");
        }
        repository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletar(Autor autor) {
        if(possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException("Autor não pode ser deletado pois possui livro(s) cadastrado(s)");
        }
        repository.delete(autor);
    }

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

    // pesquisa por params
    public List<Autor> pesquisaByExample(String nome, String nacionalidade) {
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "dataNascimento", "dataCadastro")
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Autor> autorExample = Example.of(autor, matcher);
        return repository.findAll(autorExample);
    }
}
