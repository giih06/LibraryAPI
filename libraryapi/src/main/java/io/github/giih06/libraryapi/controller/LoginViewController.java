package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.securty.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @GetMapping
    @ResponseBody
    public String paginaHome(Authentication authentication) {
        if (authentication instanceof CustomAuthentication customAuthentication) {
            System.out.println(customAuthentication.getUsuario());
        }
        return "Olá " + authentication.getName();
    }
}
