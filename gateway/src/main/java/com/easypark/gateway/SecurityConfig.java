package com.easypark.gateway;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // Injeta o repositório de registro de clientes OAuth2, utilizado para gerenciar registros de clientes OAuth2.

    @Autowired
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    // Injeta o URI do JWK Set (JSON Web Key Set) presente no `application.yaml`.

    @Value("${spring.security.oauth2.client.provider.azure.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${LOGOUT_URI}")  // Ex: URI de logout do Azure
    private String logoutURI;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable);

        // - Permite acesso público ao caminho "/login" e requer autenticação para qualquer outro caminho.

        http.authorizeExchange(conf -> conf
                        // Permite que os endpoints /login e /logout sejam acessados sem autenticação
                        .pathMatchers("/login", "/logout").permitAll()
                        // Qualquer outro endpoint só pode ser acessado com autenticação
                        .anyExchange().authenticated())
                // Configurando a aplicação com o OAuth2,
                .oauth2Login(conf -> conf
                        // Definindo o endpoint de redirecionamento após o login ser feito com sucesso
                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("http://localhost:5173/home")))
                // Definindo a proteção da aplicação com tokens JWT
                .oauth2ResourceServer(conf -> conf
                        // Definindo o decodificador JWT para a validação e interpretação dos tokens recebidos
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder())))
                // Configuração do logout
                .logout(logout -> logout
                        // Quando o logout for feito com sucesso, esse interpretador será utilizado
                        .logoutSuccessHandler(azureLogoutSuccessHandler()));
        return http.build();
    }

    // Cria um bean para o decodificador de JWTs usando o URI do JWK SET que configuramos acima!.
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public ServerLogoutSuccessHandler azureLogoutSuccessHandler() {
        return (exchange, authentication) -> {
            ServerWebExchange webExchange = exchange.getExchange();
            // Redireciona o usuário para o endpoint de logout do Azure
            webExchange.getResponse().setStatusCode(HttpStatus.FOUND);
            webExchange.getResponse().getHeaders().setLocation(URI.create(logoutURI));
            return webExchange.getResponse().setComplete();
        };
    }
}
