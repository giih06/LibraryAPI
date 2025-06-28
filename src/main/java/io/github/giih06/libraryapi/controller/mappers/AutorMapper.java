package io.github.giih06.libraryapi.controller.mappers;

import io.github.giih06.libraryapi.controller.dto.AutorDTO;
import io.github.giih06.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper responsável por converter entre Autor e AutorDTO.
 *
 * Utiliza a biblioteca MapStruct para geração automática de código de mapeamento.
 */
@Mapper(componentModel = "spring") // Permite que o Spring gerencie a instância do mapper como um bean
public interface AutorMapper {

    /**
     * Converte um DTO de Autor em uma entidade Autor.
     *
     * @param dto objeto AutorDTO com dados a serem convertidos
     * @return objeto Autor (entidade JPA)
     */
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "dataNascimento", target = "dataNascimento")
    @Mapping(source = "nacionalidade", target = "nacionalidade")
    Autor toEntity(AutorDTO dto);

    /**
     * Converte uma entidade Autor em seu respectivo DTO.
     *
     * @param autor objeto Autor (entidade)
     * @return objeto AutorDTO com os dados convertidos
     */
    AutorDTO toDto(Autor autor);
}
