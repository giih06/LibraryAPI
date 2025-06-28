package io.github.giih06.libraryapi.controller.mappers;

import io.github.giih06.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.giih06.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper responsável por conversões entre Livro, CadastroLivroDTO e ResultadoPesquisaLivroDTO.
 *
 * A injeção de dependência do AutorRepository permite mapear o ID do autor
 * para a entidade Autor ao criar o objeto Livro.
 */
@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    /**
     * Converte um DTO de cadastro de livro em uma entidade Livro.
     *
     * Necessário buscar o Autor correspondente ao ID informado.
     *
     * @param dto objeto CadastroLivroDTO com dados de entrada
     * @return entidade Livro pronta para ser persistida
     */
    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    /**
     * Converte uma entidade Livro em um DTO para resposta de pesquisa/listagem.
     *
     * @param livro objeto Livro persistido
     * @return DTO com os dados organizados para exibição
     */
    public abstract ResultadoPesquisaLivroDTO toDto(Livro livro);
}
