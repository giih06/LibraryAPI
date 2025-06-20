package io.github.giih06.libraryapi.controller.mappers;

import io.github.giih06.libraryapi.controller.dto.AutorDTO;
import io.github.giih06.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")// transforma em um coponente
public interface AutorMapper {

    // transfoma dto em entitade
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "dataNascimento", target = "dataNascimento")
    @Mapping(source = "nacionalidade", target = "nacionalidade")
    Autor toEntity(AutorDTO dto);

    // transforma entidade em dto
    AutorDTO toDto(Autor autor);
}
