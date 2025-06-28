package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.controller.dto.AutorDTO;
import io.github.giih06.libraryapi.controller.mappers.AutorMapper;
import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador REST responsável pelas operações relacionadas aos autores.
 * Contém endpoints para cadastro, busca, atualização e exclusão de autores.
 */
@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores") // Tag de agrupamento para documentação Swagger
@Slf4j
public class AutorController implements GenericController {

    private final AutorService service;
    private final AutorMapper mapper;

    /**
     * Endpoint para cadastrar um novo autor.
     * Apenas usuários com papel "GERENTE" podem acessar.
     *
     * @param dto dados do autor a ser cadastrado
     * @return resposta com status 201 (Created) e location do novo recurso
     */
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid AutorDTO dto) {
        log.info("Cadastrando novo autor: {} ", dto.nome());
        Autor autor = mapper.toEntity(dto);
        service.salvar(autor);
        URI location = gerarHeaderLocation(autor.getId());
        return ResponseEntity.created(location).build();
    }

    /**
     * Retorna os detalhes de um autor pelo seu ID.
     * Acesso permitido a operadores e gerentes.
     *
     * @param id identificador do autor
     * @return dados do autor, ou 404 se não encontrado
     */
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do Autor pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor Encontrado."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    })
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        return service
                .obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDto(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deleta um autor existente pelo ID.
     * Apenas gerentes podem realizar essa operação.
     *
     * @param id identificador do autor
     * @return status 204 (No Content) se deletado com sucesso, 404 se não encontrado
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "400", description = "Autor possui livro cadastrado.")
    })
    public ResponseEntity<Void> excluir(@PathVariable("id") String id) {
        log.info("Deletando autor de ID: {} ", id);
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(autorOptional.get());

        return ResponseEntity.noContent().build();
    }

    /**
     * Realiza pesquisa de autores com base nos parâmetros opcionais: nome e nacionalidade.
     * Acesso permitido a operadores e gerentes.
     *
     * @param nome nome do autor (opcional)
     * @param nacionalidade nacionalidade do autor (opcional)
     * @return lista de autores encontrados
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar", description = "Realiza pesquisa de autores por parâmetros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso.")
    })
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {
        List<Autor> resultadoPesquisa = service.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> lista = resultadoPesquisa
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    /**
     * Atualiza os dados de um autor existente.
     * Apenas gerentes podem atualizar.
     *
     * @param id identificador do autor a ser atualizado
     * @param dto dados atualizados do autor
     * @return status 204 (No Content) se atualizado, ou 404 se autor não for encontrado
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Void> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AutorDTO dto) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        /*
         * Não utilizamos o mapper.toEntity(dto) aqui para evitar sobrescrever
         * atributos não presentes no DTO com null. Apenas os campos atualizáveis
         * são modificados diretamente.
         */
        var autor = autorOptional.get();
        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setNacionalidade(dto.nacionalidade());

        service.atualizar(autor);

        return ResponseEntity.noContent().build();

    }
}
