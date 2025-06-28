package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Handler personalizado executado após login social bem-sucedido (OAuth2).
 * Responsável por cadastrar o usuário na base de dados, se necessário, e ajustar o contexto de segurança.
 */
@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    // Senha padrão usada para usuários cadastrados via login social
    private static final String SENHA_PADRAO = "abc";

    // Serviço responsável por operações com a entidade Usuario
    private final UsuarioService usuarioService;

    /**
     * Metodo chamado automaticamente após autenticação social bem-sucedida.
     * Verifica se o usuário existe no banco e o registra se for um novo.
     */
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // Recupera o token e os dados do usuário autenticado via OAuth2
        OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2Authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");  // extrai o e-mail do usuário

        // Busca o usuário pelo e-mail no banco de dados
        Usuario usuario = usuarioService.buscarPorEmail(email);

        // Cadastra o usuário se ele ainda não existir
        usuario = cadastrarUsuarioNaBase(usuario, email);

        // Substitui o Authentication no contexto por um customizado
        authentication = new CustomAuthentication(usuario);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println(email);

        // Continua o fluxo padrão de autenticação após login
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * Registra o usuário na base de dados caso ele ainda não exista.
     * Define login com base no e-mail, senha padrão e papel "OPERADOR".
     */
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

    /**
     * Extrai o login a partir do e-mail, removendo o domínio.
     * Exemplo: "maria@gmail.com" → "maria"
     */
    private String obterLoginApartirDoEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
