package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String SENHA_PADRAO = "abc";

    private final UsuarioService usuarioService;

    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2Authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        Usuario usuario = usuarioService.buscarPorEmail(email);

        usuario = cadastrarUsuarioNaBase(usuario, email);

        authentication = new CustomAuthentication(usuario);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println(email);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private Usuario cadastrarUsuarioNaBase(Usuario usuario, String email) {
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setLogin(obterLoginApartirDoEmail(email));
            usuario.setSenha(SENHA_PADRAO);
            usuario.setRoles(List.of("OPERADOR"));
            usuarioService.salvar(usuario);
        }
        return usuario;
    }

    private String obterLoginApartirDoEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
