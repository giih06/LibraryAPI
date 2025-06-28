package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Serviço responsável por fornecer informações de segurança relacionadas ao usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class SecurityService {

    // Serviço utilizado para operações com a entidade Usuario (não usado neste trecho, mas útil para extensões futuras)
    private final UsuarioService usuarioService;

    /**
     * Obtém o usuário atualmente autenticado no contexto de segurança.
     *
     * @return o objeto Usuario autenticado, ou null caso não esteja autenticado via CustomAuthentication
     */
    public Usuario obterUsuarioLogado() {
        // Recupera o objeto de autenticação do contexto do Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se a autenticação é do tipo CustomAuthentication e retorna o usuário
        if (authentication instanceof CustomAuthentication customAuthentication) {
            return customAuthentication.getUsuario();
        }

        // Retorna null caso não haja autenticação válida ou não seja do tipo esperado
        return null;
    }
}
