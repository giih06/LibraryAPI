package io.github.giih06.libraryapi.repository.specs;

import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Classe utilitária que fornece Specifications dinâmicas para consultas
 * personalizadas sobre a entidade Livro utilizando a API Criteria do JPA.
 *
 * Utilizada em repositórios com suporte a {@code JpaSpecificationExecutor}.
 */
public class LivroSpecs {

    /**
     * Cria uma Specification para filtrar livros pelo ISBN exato.
     *
     * @param isbn valor exato do ISBN a ser comparado
     * @return Specification para aplicar em consultas com filtro por ISBN
     */
    public static Specification<Livro> isbnEqual(String isbn){
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    /**
     * Cria uma Specification para filtrar livros por título, usando LIKE (case-insensitive).
     *
     * Equivalente a: {@code upper(livro.titulo) LIKE %:titulo%}
     *
     * @param titulo fragmento de texto a ser pesquisado no título
     * @return Specification para aplicar em consultas por título
     */
    public static Specification<Livro> tituloLike(String titulo){
        // upper(livro.titulo) like (%:param%)
        return (root, query, cb) ->
                cb.like( cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    /**
     * Cria uma Specification para filtrar livros por gênero (enum).
     *
     * @param genero valor do enum {@link GeneroLivro} a ser comparado
     * @return Specification para aplicar em consultas por gênero
     */
    public static Specification<Livro> generoEqual(GeneroLivro genero){
        return (root, query, cb) -> cb.equal(root.get("genero"), genero);
    }

    /**
     * Cria uma Specification para filtrar livros pelo ano de publicação.
     *
     * Utiliza a função do banco {@code to_char(data_publicacao, 'YYYY')} para extrair o ano.
     *
     * @param anoPublicacao ano desejado para filtro (ex: 2020)
     * @return Specification para aplicar em consultas por ano
     */
    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao){
        // and to_char(data_publicacao, 'YYYY') = :anoPublicacao
        return (root, query, cb) ->
                cb.equal( cb.function("to_char", String.class,
                        root.get("dataPublicacao"), cb.literal("YYYY")),anoPublicacao.toString());
    }

    /**
     * Cria uma Specification para filtrar livros pelo nome do autor (case-insensitive).
     *
     * Realiza um JOIN entre Livro e Autor, e aplica o filtro no campo {@code autor.nome}.
     *
     * @param nome fragmento do nome do autor a ser pesquisado
     * @return Specification para aplicar em consultas por nome de autor
     */
    public static Specification<Livro> nomeAutorLike(String nome){
        return (root, query, cb) -> {
            Join<Object, Object> joinAutor = root.join("autor", JoinType.INNER);
            return cb.like( cb.upper(joinAutor.get("nome")), "%" + nome.toUpperCase() + "%" );

//            return cb.like( cb.upper(root.get("autor").get("nome")), "%" + nome.toUpperCase() + "%" );
        };
    }
}
