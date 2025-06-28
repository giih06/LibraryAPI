package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementação personalizada da interface Authentication do Spring Security.
 * Representa a autenticação baseada no usuário da aplicação.
 */
@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    // Instância do usuário autenticado
    private final Usuario usuario;

    /**
     * Retorna as autoridades (perfis) do usuário, convertendo as roles (strings) em objetos do tipo GrantedAuthority.
     */    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.usuario
                .getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()); // stream.map pois é uma lista de strings
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * Define se a instância está autenticada (método obrigatório da interface).
     * Neste caso, a lógica não é implementada.
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // Metodo não utilizado; poderia lançar exceção caso necessário.

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
