package io.github.giih06.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioDTO(
        @NotBlank(message = "Login é obrigatório!")
        String login,
        @NotBlank(message = "Senha é obrigatório!")
        String senha,
        @Email(message = "email inválido")
        @NotBlank(message = "Email é obrigatório!")
        String email,
        List<String> roles
) {
}
