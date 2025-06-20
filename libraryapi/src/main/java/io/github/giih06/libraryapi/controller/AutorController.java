package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.controller.dto.AutorDTO;
import io.github.giih06.libraryapi.controller.mappers.AutorMapper;
import io.github.giih06.libraryapi.model.Autor;
import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.securty.SecurityService;
import io.github.giih06.libraryapi.service.AutorService;
import io.github.giih06.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController implements GenericController {

    private final AutorService service;
    private final AutorMapper mapper;
    private final SecurityService securityService;

    // Authentication guarda o nome do usuário que fizer a requisição
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> salvar(@RequestBody @Valid AutorDTO dto) {
        Autor autor = mapper.toEntity(dto);
        service.salvar(autor);
        URI location = gerarHeaderLocation(autor.getId());
        return ResponseEntity.created(location).build();

    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        return service
                .obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDto(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> excluir(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(autorOptional.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
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

    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AutorDTO dto) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        /* não é utilizado o mapper neste caso pois o mapper atribui valor
          null aos atributos que não estão no DTO e nesse caso os outros
         atributos que não serão atualizados devem permanecer com seu valor */
        var autor = autorOptional.get();
        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setNacionalidade(dto.nacionalidade());

        service.atualizar(autor);

        return ResponseEntity.noContent().build();

    }
}
