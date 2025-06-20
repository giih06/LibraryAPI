package io.github.giih06.libraryapi.repository;

import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest() {
        Livro livro = new Livro();
        livro.setTitulo("Mundo das Fadas");
        livro.setIsbn("93434-12144");
        livro.setPreco(BigDecimal.valueOf(19));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setDataPublicacao(LocalDate.of(2014, 3, 22));

// Adicionando um autor já existente do banco de dados
        Autor autorMaria = autorRepository
                .findById(UUID.fromString("b6522dfb-2d9c-42bd-a991-81566d48ccc0"))
                .orElse(null);

        livro.setAutor(autorMaria);

        livroRepository.save(livro);
    }

    @Test
    void salvarCascadeTest() {
        Livro livro = new Livro();
        livro.setTitulo("Mundo das Fadas");
        livro.setIsbn("93434-12144");
        livro.setPreco(BigDecimal.valueOf(19));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setDataPublicacao(LocalDate.of(2014, 3, 22));

        // Criando um autor ( metodo cascade)

        Autor autor = new Autor();
        autor.setNome("Pedro");
        autor.setNacionalidade("Estadunidense");
        autor.setDataNascimento(LocalDate.of(1990, 8, 11));

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    // Metodo mais seguro, padrão e mais utilizado pelos programadores
    @Test
    void salvarAutorELivroTest() {
        Livro livro = new Livro();
        livro.setTitulo("O pequeno Príncipe");
        livro.setIsbn("32234-12144");
        livro.setPreco(BigDecimal.valueOf(69));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setDataPublicacao(LocalDate.of(2010, 6, 21));

        // Criando um autor sem cascade

        Autor autor = new Autor();
        autor.setNome("Joanna");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1991, 12, 27));

        autorRepository.save(autor);
        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void atualizarAutorDoLivro() {
        UUID idLivro = UUID.fromString("854c863b-381e-407e-8199-af6c0bb540c2");
        var livroParaAtualizar = livroRepository.findById(idLivro).orElse(null);

        UUID idAutor = UUID.fromString("bcc5d34e-4859-4a86-ba4d-2c9cae47bce1");
        Autor joanna = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(joanna);

        livroRepository.save(livroParaAtualizar);
    }

    @Test
    void deletar() {
        UUID idLivro = UUID.fromString("854c863b-381e-407e-8199-af6c0bb540c2");
        livroRepository.deleteById(idLivro);
    }

    // habilitar cascade all na entidade livro
    @Test
    void deletarCascade() {
        UUID idLivro = UUID.fromString("967e2466-2bdc-427b-b959-4a561e4ab7da");
        livroRepository.deleteById(idLivro);
    }

    @Test
    @Transactional
    void buscarLivroTest() {
        UUID id = UUID.fromString("967e2466-2bdc-427b-b959-4a561e4ab7da");
        Livro livro = livroRepository.findById(id).orElse(null);
        System.out.println("Livro: ");
        System.out.println(livro.getTitulo());
    //  System.out.println("Autor: ");
    //  System.out.println(livro.getAutor().getNome());
    }

    @Test
    void pesquisarPorTituloTest() {
        List<Livro> lista = livroRepository.findByTitulo("A Branca de Neve");
        lista.forEach(System.out::println);
    }

    @Test
    void pesquisarPorISBNTest() {
        Optional<Livro> livro = livroRepository.findByIsbn("32234-53322");
        livro.ifPresent(System.out::println);
    }

    @Test
    void pesquisarPorTituloEPrecoTest() {
        var preco = BigDecimal.valueOf(69.00);
        List<Livro> lista = livroRepository.findByTituloAndPreco("O pequeno Príncipe", preco);
        lista.forEach(System.out::println);
    }

    @Test
    void listarLivrosComQueryJPQL() {
        var resultado = livroRepository.listarTodosOrdenadoPorTituloAndPreco();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarAutoresDosLivros() {
        var resultado = livroRepository.listarAutoresDosLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarTitulosNaoRepetidosDosLivros() {
        var resultado = livroRepository.listarNomesDiferentesLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarGenerosDeLivrosAutoresBrasileiros() {
        var resultado = livroRepository.listarGenerosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParam() {
        var resultado = livroRepository.findByGenero(GeneroLivro.MISTERIO, "preco");
        resultado.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroPositionalQueryParam() {
        var resultado = livroRepository.findByGeneroPositionalParameters(GeneroLivro.MISTERIO, "preco");
        resultado.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTest() {
        livroRepository.deleteByGenero(GeneroLivro.MISTERIO);
    }

    @Test
    void updateDataPublicacaoTest() {
        livroRepository.updateDataPublicacao(LocalDate.of(2000,1,1));
    }
}