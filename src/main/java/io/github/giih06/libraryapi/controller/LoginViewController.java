package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.securty.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador responsável por gerenciar as views relacionadas à autenticação.
 * Fornece endpoints para login, home e exibição do authorization code.
 */
@Controller
public class LoginViewController {

    /**
     * Renderiza a página de login.
     * Mapeia a URL "/login" para retornar o template "login.html".
     *
     * @return nome da view de login
     */
    @GetMapping("/login")
    public String paginaLogin() {
        return "login";  // Retorna a view login.html (sem @ResponseBody, portanto resolve um template)
    }

    /**
     * Endpoint principal (raiz "/") que retorna uma mensagem personalizada
     * com base no usuário autenticado.
     *
     * @param authentication objeto fornecido pelo Spring Security com os dados do usuário logado
     * @return saudação com o nome do usuário autenticado
     */
    @GetMapping
    @ResponseBody
    public String paginaHome(Authentication authentication) {
        // Verifica se é uma instância personalizada da autenticação
        if (authentication instanceof CustomAuthentication customAuthentication) {
            System.out.println(customAuthentication.getUsuario()); // Exibe os dados do usuário no console
        }

        return "Olá " + authentication.getName(); // Retorna saudação personalizada
    }

    /**
     * Endpoint para capturar o Authorization Code recebido do servidor de autorização.
     *
     * @param code código de autorização fornecido como parâmetro na URL
     * @return mensagem exibindo o código recebido
     */
    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code) {
        return "Seu authorization code: " + code;
    }
}
