package io.github.giih06.libraryapi.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.giih06.libraryapi.securty.CustomAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Configuração do servidor de autorização OAuth2, definindo filtros de segurança,
 * beans de token, codificação de senha e endpoints de autorização.
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    /**
     * Configura a SecurityFilterChain para o Authorization Server,
     * habilitando endpoints OAuth2, OIDC e JWT Resource Server.
     *
     * @param http objeto configurador de segurança HTTP
     * @return a cadeia de filtros de segurança construída
     * @throws Exception em caso de erro na configuração do HttpSecurity
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // Aplica configurações padrão para Authorization Server
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // Habilita suporte a OpenID Connect
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        // Configura JWT como metodo de validação de resource server
        http.oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()));

        // Página de login customizada
        http.formLogin(configurer -> configurer.loginPage("/login"));

        return http.build();
    }

    /**
     * Bean para codificação de senhas usando BCrypt com strength 10.
     *
     * @return o PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Define as configurações de tempo de vida e formato dos tokens.
     *
     * @return TokenSettings contendo TTL e formato (self-contained)
     */
    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                // access_token: token utilizado nas requisições
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                // refresh_token: token para renovar o access_token
                .refreshTokenTimeToLive(Duration.ofMinutes(90))
                .build();
    }

    /**
     * Ajusta configurações do cliente OAuth2, como a exigência de consentimento.
     *
     * @return ClientSettings com consentimento não obrigatório
     */
    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

    /**
     * Gera e expõe um JWKSource baseado em par de chaves RSA.
     *
     * @return fonte de chaves JSON Web Key imutável
     * @throws Exception se ocorrer erro na geração do par de chaves
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        // JWK - JSON Web Key
        RSAKey rsaKey = gerarChaveRSA();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Gera um par de chaves RSA 2048 bits e associa um key ID gerado aleatoriamente.
     *
     * @return RSAKey contendo chave pública e privada
     * @throws Exception em caso de falha na instância do gerador RSA
     */
    private RSAKey gerarChaveRSA() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey chavePublica = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey chavePrivada = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
                .Builder(chavePublica)
                .privateKey(chavePrivada)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    /**
     * Configura o JwtDecoder baseado no JWKSource gerado.
     *
     * @param jwkSource fonte de chaves JWK
     * @return JwtDecoder configurado pelo OAuth2AuthorizationServerConfiguration
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * Define endpoints customizados para o servidor de autorização (token, introspect, revoke, etc.).
     *
     * @return AuthorizationServerSettings com URIs dos endpoints OAuth2/OIDC
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                // obter token
                .tokenEndpoint("/oauth2/token")
                // para consultar status do token
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                // para revogar o token
                .tokenRevocationEndpoint("/oauth2/revoke")
                // authorization endpoint
                .authorizationEndpoint("/oauth2/authorize")
                // informações do usuário UPEN ID CONNECT
                .oidcUserInfoEndpoint("/oauth2/iserinfo")
                // obter a chave publica para verificar a assinatura do token
                .jwkSetEndpoint("/oauth2/jwks")
                // fazer logout
                .oidcLogoutEndpoint("/oauth2/logout")
                .build();
    }

    /**
     * Personaliza o conteúdo dos tokens JWT para incluir autoridades e email do usuário autenticado.
     *
     * @return OAuth2TokenCustomizer que insere claims adicionais no token de acesso
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(){
        return context -> {
            var principal = context.getPrincipal();

            if(principal instanceof CustomAuthentication authentication){
                OAuth2TokenType tipoToken = context.getTokenType();

                if(OAuth2TokenType.ACCESS_TOKEN.equals(tipoToken)){
                    Collection<GrantedAuthority> authorities = authentication.getAuthorities();
                    List<String> authoritiesList =
                            authorities.stream().map(GrantedAuthority::getAuthority).toList();

                    context
                            .getClaims()
                            .claim("authorities", authoritiesList)
                            .claim("email", authentication.getUsuario().getEmail());
                }
            }

        };
    }
}
