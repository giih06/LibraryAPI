package io.github.giih06.libraryapi.repository;

import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Repositório para operações de persistência com a entidade Livro.
 */
public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    // Query Method
    // SELECT * FROM livro WHERE id_autor = id
    List<Livro> findByAutor(Autor autor);

    // Pesquisar livro por Título
    List<Livro> findByTitulo(String titulo);

    // Pesquisar livro por ISBN
    Optional<Livro> findByIsbn(String isbn);

    // Pesquisar livro por titulo e preço
    // SELECT * FROM livro WHERE titulo = ? AND preco = ?
    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    // Pesquisar livro por titulo ou isbn
    // SELECT * FROM livro WHERE titulo = ? OR isbn = ?
    List<Livro> findByTituloOrIsbn(String titulo, String isbn);

    // SELECT * FROM livro where data_publicacao between ? and ?;
    List<Livro> findByDataPublicacaoBetween(LocalDate inicio, LocalDate fim);


    // Annotations @Query //

    // JPQL -> referencia as entidades e as propriedades das entidades
    @Query(" SELECT l FROM Livro AS l ORDER BY l.titulo, l.preco ")
    List<Livro> listarTodosOrdenadoPorTituloAndPreco();

    /**
     * SELECT a.*
     * FROM livro l
     * JOIN autor a ON a.id = l.id_autor
     */
    @Query(" SELECT a FROM Livro l JOIN l.autor a ")
    List<Autor> listarAutoresDosLivros();

    /**
     * SELECT DISTINCT l.*
     * FROM livro l
     */
    @Query(" SELECT DISTINCT l.titulo FROM Livro l ")
    List<String> listarNomesDiferentesLivros();

    /**
     * SELECT DISTINCT l.genero
     * FROM livro l
     * JOIN autor a ON a.id = l.id_autor
     * WHERE a.nacionalidade = 'Brasileira'
     * order by l.genero
     */
    @Query("""
        SELECT l.genero
        FROM Livro l
        JOIN l.autor a
        WHERE a.nacionalidade = 'Brasileira'
        ORDER BY l.genero
    """)
    List<String> listarGenerosAutoresBrasileiros();

    // named parameters -> parametros nomeados
    @Query("select l from Livro l where l.genero = :genero order by :paramOrdenacao ")
    List<Livro> findByGenero(
            @Param("genero") GeneroLivro generoLivro,
            @Param("paramOrdenacao") String nomePropriedade
    );

    // positional parameters
    @Query(" SELECT l FROM Livro l WHERE l.genero = ?1 ORDER BY ?2 ")
    List<Livro> findByGeneroPositionalParameters(GeneroLivro generoLivro, String nomePropriedade
    );

    // Operação delete usando @Query
    @Transactional
    @Modifying //annotation que permite a modificação de registros ( operação delete )
    @Query(" DELETE FROM Livro WHERE genero = ?1 ")
    void deleteByGenero(GeneroLivro generoLivro);

    // Operação update usando @Query
    @Transactional
    @Modifying //annotation que permite a modificação de registros ( operação delete )
    @Query(" UPDATE Livro set dataPublicacao = ?1 ")
    void updateDataPublicacao(LocalDate novaData);

    boolean existsByAutor(Autor autor);
}
