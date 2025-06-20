package io.github.giih06.libraryapi.controller.mappers;

import io.github.giih06.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.giih06.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.giih06.libraryapi.model.Livro;
import io.github.giih06.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    // transfoma dto de cadastro em entitade
    // Necessário transformar o id do autor na entidade autor
    //@Mapping(target = "autor", source = "idAutor")
    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    public abstract ResultadoPesquisaLivroDTO toDto(Livro livro);
}
