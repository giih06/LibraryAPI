package io.github.giih06.libraryapi.config;

import io.github.giih06.libraryapi.securty.CustomUserDetailsService;
import io.github.giih06.libraryapi.securty.LoginSocialSuccessHandler;
import io.github.giih06.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // habilita as autorizações de requisições http diretamente nos controllers
public class  SecurityConfiguration {

    // Configuração padrão do spring security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler successHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // disabilita a proteção para que páginas web de que outras aplicações consigam fazer requisições
                .httpBasic(Customizer.withDefaults())
                .formLogin(configurer -> {
                    configurer.loginPage("/login").permitAll();
               })
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> {
                    oauth2
                            .loginPage("/login")
                            .successHandler(successHandler);
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

//    @Bean
    public UserDetailsService userDetailsService(UsuarioService usuarioService) {
//        UserDetails user1 = User.builder()
//                .username("usuario")
//                .password(passwordEncoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("321"))
//                .roles("ADMIN")
//                .build();
//
//
//        return new InMemoryUserDetailsManager(user1, user2);
        return new CustomUserDetailsService(usuarioService);
    }

    // CONFIGURA O PREFIXO ROLE
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
