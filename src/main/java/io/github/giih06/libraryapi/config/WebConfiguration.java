package io.github.giih06.libraryapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração personalizada do Spring MVC para a aplicação.
 *
 * Define controladores de visualização (ViewControllers) sem lógica de negócio,
 * como a página de login.
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * Mapeia diretamente uma URL para uma view, sem a necessidade de um controller.
     *
     * Neste caso:
     * - A URL "/login" é associada à view chamada "login" (ex: login.html)
     * - A ordem de prioridade é definida como a mais alta, garantindo o mapeamento imediato
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login"); // Mapeia /login para a view "login"
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);  // Define prioridade máxima para este mapeamento
    }
}
