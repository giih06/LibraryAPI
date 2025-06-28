package io.github.giih06.libraryapi.service;

import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.repository.AutorRepository;
import io.github.giih06.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Serviço responsável por operações transacionais envolvendo livros e autores.
 * As transações garantem atomicidade entre múltiplas operações no banco.
 */
@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroRepository livroRepository;

    // livro (titulo,..., nome_arquivo) -> id.png
    @Transactional
    public void salvarLivroComFoto() {
        // salvar o livro
        // repository.save(livro);

        // pega o id do livro = livro.getId();
        // var id = livro.getId();

        // salvar foto do livro -> bucker na nuvem
        // bucketService.salvar(livro.getFoto(), id + ".png");

        // atualizar o nome do arquivo que foi salvo
        // livro.setNomeArquivoFoto(id + ".png");
    }

    /**
     * Simula uma atualização em uma entidade gerenciada pelo JPA,
     * sem a necessidade de chamar o metodo save explicitamente.
     * O commit da transação irá aplicar a mudança automaticamente.
     */
    @Transactional
    public void atualizacaoSemAtualizar() {
        var livro = livroRepository
                .findById(UUID.fromString("967e2466-2bdc-427b-b959-4a561e4ab7da"))
                .orElse(null);

        // Atualiza um campo da entidade carregada
        // Mesmo sem chamar save(), a alteração será persistida no commit da transação
        livro.setDataPublicacao(LocalDate.of(2024, 9, 1));
    }

    /**
     * Exemplo de transação completa com rollback automático em caso de exceção.
     * Salva um autor e um livro associado a ele. Lança uma exceção ao final para simular falha.
     *
     * A anotação @Transactional garante que ambas as operações sejam revertidas caso ocorra erro.
     */
    @Transactional
    public void executaTransacao() {
        // salvam o autor
        Autor autor = new Autor();
        autor.setNome("Teste Nandinha");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1991, 12, 27));
        autorRepository.save(autor);

        // salva o livro
        Livro livro = new Livro();
        livro.setTitulo("Coding Test da NANDA");
        livro.setIsbn("32234-12144");
        livro.setPreco(BigDecimal.valueOf(69));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setDataPublicacao(LocalDate.of(2010, 6, 21));
        livro.setAutor(autor);
        livroRepository.save(livro);

        if (autor.getNome().equals("Teste Nandinha")) {
            throw new RuntimeException("Rollback!");
        }
    }
}
