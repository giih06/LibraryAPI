package io.github.giih06.libraryapi.controller.mappers;

import io.github.giih06.libraryapi.controller.dto.UsuarioDTO;
import io.github.giih06.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);
}
