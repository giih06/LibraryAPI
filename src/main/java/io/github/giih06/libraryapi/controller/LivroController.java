package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.giih06.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.giih06.libraryapi.controller.mappers.LivroMapper;
import io.github.giih06.libraryapi.model.GeneroLivro;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador responsável pelos endpoints relacionados ao gerenciamento de livros.
 * Permite operações de CRUD e pesquisa de livros.
 */
@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros") // Tag de agrupamento para documentação Swagger
public class LivroController implements GenericController {

    private final LivroService service;
    private final LivroMapper mapper;

    /**
     * Cadastra um novo livro.
     *
     * @param dto objeto com os dados do livro a ser salvo
     * @return resposta com status 201 e header Location apontando para o novo recurso
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo livro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado.")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        service.salvar(livro);
        var url = gerarHeaderLocation(livro.getId()); // Gera URI para Location header
        return ResponseEntity.created(url).build();
    }

    /**
     * Retorna os detalhes de um livro específico pelo ID.
     *
     * @param id identificador do livro
     * @return dados do livro ou 404 caso não seja encontrado
     */
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do Livro pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro Encontrado."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    })
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(
            @PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = mapper.toDto(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    /**
     * Deleta um livro pelo ID.
     *
     * @param id identificador do livro
     * @return 204 se deletado, ou 404 se não encontrado
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um livro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    })
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


    /**
     * Pesquisa livros com base em filtros opcionais como ISBN, título, autor, gênero e ano.
     * Também permite paginação.
     *
     * @param isbn            filtro por ISBN
     * @param titulo          filtro por título
     * @param nomeAutor       filtro por nome do autor
     * @param genero          filtro por gênero
     * @param anoPublicacao   filtro por ano de publicação
     * @param pagina          número da página (default 0)
     * @param tamanhoPagina   tamanho da página (default 10)
     * @return página contendo os resultados da pesquisa
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar", description = "Realiza pesquisa de livros por parâmetros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso.")
    })
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nomeAutor", required = false) String nomeAutor,
            @RequestParam(value = "genero", required = false) GeneroLivro genero,
            @RequestParam(value = "anoPublicacao", required = false) Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanhoPagina", defaultValue = "10") Integer tamanhoPagina
    ){
        Page<Livro> paginaResultado = service.pesquisa(
                isbn, titulo, nomeAutor, genero, anoPublicacao, pagina,tamanhoPagina
        );

        // Conversão dos resultados para DTOs de resposta
        Page<ResultadoPesquisaLivroDTO> resultado = paginaResultado.map(mapper::toDto);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Atualiza os dados de um livro existente.
     *
     * @param id  identificador do livro a ser atualizado
     * @param dto objeto com os novos dados do livro
     * @return 204 se atualizado com sucesso, 404 se não encontrado
     */
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza livro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado.")
    })
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
