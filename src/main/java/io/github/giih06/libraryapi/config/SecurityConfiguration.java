package io.github.giih06.libraryapi.config;

import io.github.giih06.libraryapi.securty.JwtCustomAuthenticationFilter;
import io.github.giih06.libraryapi.securty.LoginSocialSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

 /**
 * Classe responsável pela configuração de segurança da aplicação com Spring Security.
 * Define autenticação com JWT, login com OAuth2, filtros personalizados e controle de acesso por rotas.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // habilita as autorizações de requisições http diretamente nos controllers
public class  SecurityConfiguration {

     /**
      * Define a cadeia de filtros de segurança da aplicação.
      *
      * Configura:
      * - Login via formulário personalizado
      * - Autorização para rotas públicas e protegidas
      * - Login com provedores OAuth2 (ex: Google)
      * - Validação de tokens JWT
      * - Inclusão de filtro customizado após o filtro padrão do Bearer Token
      */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSocialSuccessHandler successHandler,
            JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // disabilita a proteção para que páginas web de que outras aplicações consigam fazer requisições
                .formLogin(configurer -> {
                    configurer.loginPage("/login").permitAll(); // Página de login customizada acessível a todos
               })
                .authorizeHttpRequests(authorize -> {
                    // Permite acesso sem autenticação às rotas abaixo
                    authorize.requestMatchers("/login").permitAll();

                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();

                    // Todas as demais rotas exigem autenticação
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> {
                    // Configura login com OAuth2 e define o handler de sucesso (pós-login)
                    oauth2
                            .loginPage("/login")
                            .successHandler(successHandler);
                })
                .oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults())) // Ativa o suporte ao JWT como recurso protegido
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class) // Adiciona o filtro personalizado após o filtro padrão do Spring que lida com Bearer Token
                .build();
    }

     /**
      * Define quais rotas devem ser ignoradas pela segurança (sem passar pelos filtros).
      */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->  web.ignoring().requestMatchers(
                "/v2/api-docs/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**",
                "/actuator/**"
            );
    }

     /**
      * Remove o prefixo padrão "ROLE_" ao trabalhar com permissões.
      *
      * Configura o PREFIXO ROLE
      * Isso facilita o uso direto de nomes como "ADMIN" ou "USER" nas roles.
      */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

     /**
      * Configura o conversor de autenticação para JWT,
      * removendo o prefixo padrão "SCOPE_" dos authorities presentes no token.
      *
      * Isso permite um controle de autorização mais limpo nos controllers.
      */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}
