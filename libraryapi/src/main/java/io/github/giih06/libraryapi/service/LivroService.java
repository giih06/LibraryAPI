package io.github.giih06.libraryapi.service;

import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.repository.LivroRepository;
import io.github.giih06.libraryapi.securty.SecurityService;
import io.github.giih06.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.github.giih06.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;
    private final LivroValidator validator;
    private final SecurityService securityService;

    public Livro salvar(Livro livro) {
        validator.validar(livro);
        Usuario user = securityService.obterUsuarioLogado();
        livro.setIdUsuario(user.getId());
        return repository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletar(Livro livro) {
        repository.delete(livro);
    }

    // Pesquisa por isbn, título, nome autor, gênero e ano de publicação
    public Page<Livro> pesquisa(
            String isbn,
            String titulo,
            String nomeAutor,
            GeneroLivro genero,
            Integer anoPublicacao,
            Integer pagina,
            Integer tamanhoPagina) {

        // select * from livro where isbn = :isbn and nomeAutor = :nomeAutor

//        Specification<Livro> specs = Specification
//                .where(LivroSpecs.isbnEqual(isbn))
//                .and(LivroSpecs.tituloLike(titulo))
//                .and(LivroSpecs.generoEqual(genero));

        // select * from livro where 0 = 0
        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction() );

        if(isbn != null) {
            // query = query and isbn = :isbn
            specs = specs.and(isbnEqual(isbn));
        }

        if(titulo != null) {
            specs = specs.and(tituloLike(titulo));
        }

        if(genero != null) {
            specs = specs.and(generoEqual(genero));
        }

        if(anoPublicacao != null) {
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }

        if(nomeAutor != null) {
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pagerequest = PageRequest.of(pagina, tamanhoPagina);

        return repository.findAll(specs, pagerequest);
    }

    public void atualizar(Livro livro) {
        if(livro.getId() == null) {
            throw new IllegalArgumentException("O livro precisa estar cadastrado para ser atualizado");
        }

        validator.validar(livro);
        repository.save(livro);
    }
}
