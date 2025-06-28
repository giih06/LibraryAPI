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

/**
 * Serviço responsável pelas regras de negócio relacionadas à entidade Livro.
 */
@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;
    private final LivroValidator validator;
    private final SecurityService securityService;

    /**
     * Salva um novo livro no banco de dados após validação.
     * Associa o livro ao usuário atualmente autenticado.
     *
     * @param livro livro a ser salvo
     * @return livro salvo
     */
    public Livro salvar(Livro livro) {
        validator.validar(livro);
        Usuario user = securityService.obterUsuarioLogado();
        livro.setIdUsuario(user.getId());
        return repository.save(livro);
    }

    /**
     * Busca um livro por seu identificador UUID.
     *
     * @param id identificador do livro
     * @return Optional com o livro encontrado ou vazio
     */
    public Optional<Livro> obterPorId(UUID id) {
        return repository.findById(id);
    }

    /**
     * Remove um livro do banco de dados.
     *
     * @param livro livro a ser removido
     */
    public void deletar(Livro livro) {
        repository.delete(livro);
    }

    /**
     * Realiza uma pesquisa paginada de livros com base em filtros dinâmicos.
     * Utiliza Specifications do Spring Data JPA para compor a query.
     *
     * @param isbn            filtro por ISBN exato
     * @param titulo          filtro por título (parcial, case-insensitive)
     * @param nomeAutor       filtro por nome do autor (parcial)
     * @param genero          filtro por gênero do livro
     * @param anoPublicacao   filtro por ano de publicação
     * @param pagina          número da página (0-based)
     * @param tamanhoPagina   quantidade de itens por página
     * @return página de livros que atendem aos critérios
     */    public Page<Livro> pesquisa(
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

        // Aplica dinamicamente cada filtro caso tenha valor
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

        // Define a paginação da consulta
        Pageable pagerequest = PageRequest.of(pagina, tamanhoPagina);

        // Executa a busca com filtros e paginação
        return repository.findAll(specs, pagerequest);
    }

    /**
     * Atualiza um livro já existente no banco de dados.
     * Lança exceção se o livro ainda não estiver cadastrado (sem ID).
     *
     * @param livro livro com dados atualizados
     */
    public void atualizar(Livro livro) {
        if(livro.getId() == null) {
            throw new IllegalArgumentException("O livro precisa estar cadastrado para ser atualizado");
        }

        validator.validar(livro); // revalida os dados
        repository.save(livro); // atualiza no banco
    }
}
