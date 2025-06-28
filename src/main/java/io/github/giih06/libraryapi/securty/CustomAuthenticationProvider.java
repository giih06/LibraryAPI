package io.github.giih06.libraryapi.securty;

import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementação personalizada de AuthenticationProvider.
 * Responsável por autenticar usuários utilizando login e senha.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // Serviço responsável por buscar os dados do usuário
    private final UsuarioService usuarioService;

    // Componente para comparar senhas com segurança (criptografadas)
    private final PasswordEncoder passwordEncoder;

    /**
     * Realiza a autenticação do usuário com base no login e senha fornecidos.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName(); // Obtém o login informado
        String senhaDigitada = authentication.getCredentials().toString(); // Obtém a senha informada

        // Busca o usuário no banco de dados pelo login
        Usuario usuarioEncontrado = usuarioService.buscarPorLogin(login);

        // Se o usuário não for encontrado, lança exceção genérica para não expor qual campo está incorreto
        if (usuarioEncontrado == null) {
            throw getErroUsuarioNaoEncontrado();
        }

        // Compara a senha digitada com a senha criptografada armazenada no banco
        String senhaCriptografada = usuarioEncontrado.getSenha();
        boolean senhasBatem = passwordEncoder.matches(senhaDigitada, senhaCriptografada);

        // Se as senhas forem compatíveis, retorna uma instância autenticada
        if (senhasBatem) {
            return new CustomAuthentication(usuarioEncontrado);
        }

        // Caso contrário, lança exceção genérica
        throw getErroUsuarioNaoEncontrado();
    }

    /**
     * Retorna uma exceção padrão para falha de autenticação.
     * Evita revelar se o erro foi no login ou na senha.
     */
    private static UsernameNotFoundException getErroUsuarioNaoEncontrado() {
        return new UsernameNotFoundException("Usuário e/ou senha incorretos!");
    }

    /**
     * Informa se esse provider suporta o tipo de autenticação fornecido.
     * Neste caso, suporta UsernamePasswordAuthenticationToken.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
