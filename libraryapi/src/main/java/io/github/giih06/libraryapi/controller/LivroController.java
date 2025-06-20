package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.giih06.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.giih06.libraryapi.controller.mappers.LivroMapper;
import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        service.salvar(livro);
        var url = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(
            @PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = mapper.toDto(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> excluir(@PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    service.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
    @DeleteMapping("{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(autorOptional.get());

        return ResponseEntity.noContent().build();
    }*/

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nomeAutor", required = false) String nomeAutor,
            @RequestParam(value = "genero", required = false) GeneroLivro genero,
            @RequestParam(value = "anoPublicacao", required = false) Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanhoPagina", defaultValue = "") Integer tamanhoPagina
    ){
        Page<Livro> paginaResultado = service.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina,tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultado = paginaResultado.map(mapper::toDto);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO dto) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro entidadeLivro = mapper.toEntity(dto);

                    livro.setIsbn(entidadeLivro.getIsbn());
                    livro.setTitulo(entidadeLivro.getTitulo());
                    livro.setDataPublicacao(entidadeLivro.getDataPublicacao());
                    livro.setGenero(entidadeLivro.getGenero());
                    livro.setPreco(entidadeLivro.getPreco());
                    livro.setAutor(entidadeLivro.getAutor());

                    service.atualizar(livro);

                    return ResponseEntity.noContent().build();
                }).orElseGet( () -> ResponseEntity.notFound().build() );

    }
}
