package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro personalizado que intercepta requisições HTTP para transformar um JwtAuthenticationToken
 * em uma instância da autenticação personalizada CustomAuthentication.
 *
 * Essa abordagem permite associar um objeto de domínio (Usuario) diretamente ao contexto de segurança.
 */
@Component
@RequiredArgsConstructor
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    // Serviço responsável por acessar os dados do usuário no banco
    private final UsuarioService usuarioService;

    /**
     * Intercepta cada requisição para verificar e transformar o token JWT em uma autenticação personalizada.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Obtém a autenticação atual do contexto de segurança
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se a autenticação atual é baseada em JWT e precisa ser convertida
        if (deveConverter(authentication)) {
            String login = authentication.getName();
            Usuario usuario = usuarioService.buscarPorLogin(login);

            // Se o usuário existir, substitui a autenticação atual pela personalizada
            if (usuario != null) {
                authentication = new CustomAuthentication(usuario);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua o fluxo da requisição normalmente
        filterChain.doFilter(request, response);
    }

    /**
     * Verifica se o tipo da autenticação atual é JwtAuthenticationToken e pode ser convertida.
     *
     * @param authentication autenticação atual do contexto
     * @return true se deve converter; false caso contrário
     */
    private boolean deveConverter(Authentication authentication) {
        return authentication != null && authentication instanceof JwtAuthenticationToken;
    }
}
