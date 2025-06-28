package io.github.giih06.libraryapi.controller.mappers;

import io.github.giih06.libraryapi.controller.dto.UsuarioDTO;
import io.github.giih06.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

/**
 * Mapper responsável por conversão entre UsuarioDTO e a entidade Usuario.
 *
 * Utilizado principalmente para cadastro e autenticação de usuários.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    /**
     * Converte um DTO de usuário em uma entidade Usuario.
     *
     * @param dto objeto com dados recebidos da API
     * @return objeto Usuario persistível
     */
    Usuario toEntity(UsuarioDTO dto);
}
