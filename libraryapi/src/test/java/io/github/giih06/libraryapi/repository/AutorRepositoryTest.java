package io.github.giih06.libraryapi.repository;

import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest(){
        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Italiana");
        autor.setDataNascimento(LocalDate.of(1950, 1, 31));

        var autorSalvo = autorRepository.save(autor);
        System.out.println("Autor Salvo: " + autorSalvo);

    }

    @Test
    public void atualizarTest(){
        var id = UUID.fromString("a11c9c6f-755b-4b3e-ba1e-aa3de4b38b31");
        Optional<Autor> possivelAutor = autorRepository.findById(id);
        if(possivelAutor.isPresent()){
            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do Autor:");
            System.out.println(autorEncontrado);

            autorEncontrado.setDataNascimento(LocalDate.of(1960, 1, 30));

            autorRepository.save(autorEncontrado);
        }
    }

    @Test
    public void listarTest(){
        List<Autor> lista = autorRepository.findAll();
        lista.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        System.out.println("Contagem de autores: " + autorRepository.count());
    }

    @Test
    public void deletePorIdTest(){
        var id = UUID.fromString("a11c9c6f-755b-4b3e-ba1e-aa3de4b38b31");
        autorRepository.deleteById(id);
    }

    @Test
    public void deleteTest(){
        var id = UUID.fromString("ab616328-59a9-4aee-b368-98506c872bc5");
        var maria = autorRepository.findById(id).get();
        autorRepository.delete(maria);// deleta o objeto
    }

    @Test
    void salvarAutorComLivrosTest(){
        Autor autor = new Autor();
        autor.setNome("Giulianna");
        autor.setNacionalidade("Mexicana");
        autor.setDataNascimento(LocalDate.of(1930, 1, 11));

        Livro livro = new Livro();
        livro.setTitulo("A Branca de Neve");
        livro.setIsbn("32234-12222");
        livro.setPreco(BigDecimal.valueOf(25));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setDataPublicacao(LocalDate.of(2003, 6, 21));
        livro.setAutor(autor);


        Livro livro2 = new Livro();
        livro2.setTitulo("Fadas encantadas 2");
        livro2.setIsbn("32234-53322");
        livro2.setPreco(BigDecimal.valueOf(127));
        livro2.setGenero(GeneroLivro.MISTERIO);
        livro2.setDataPublicacao(LocalDate.of(2009, 8, 19));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        autorRepository.save(autor);
        // livroRepository.saveAll(autor.getLivros());
    }

    @Test
    void listarLivrosAutor() {
        var id = UUID.fromString("b6522dfb-2d9c-42bd-a991-81566d48ccc0");
        var autor = autorRepository.findById(id).get();
        
        //buscar os livros do autor
        List<Livro> livrosLista = livroRepository.findByAutor(autor);
        autor.setLivros(livrosLista);

        autor.getLivros().forEach(System.out::println);
    }
}
