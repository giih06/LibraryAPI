package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final Usuario usuario;

    // transformando as "roles" em "authorities"
    @Override
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

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
