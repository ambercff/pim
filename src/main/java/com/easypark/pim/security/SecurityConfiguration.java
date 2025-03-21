package com.easypark.pim.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                .cors(cors -> Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/users/login").permitAll(); // Verificando se o usuário está
                    // logado
                    req.requestMatchers(HttpMethod.GET, "/vagas/{numeroVaga}/veiculo-atual").hasAnyRole("ADMIN");

                    req.requestMatchers(HttpMethod.PUT, "/vagas/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/vagas", "/vagas/**").permitAll();

                    req.requestMatchers(HttpMethod.POST, "/entradaSaida/**").permitAll();
                    req.requestMatchers(HttpMethod.PUT, "/entradaSaida/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/entradaSaida/**").permitAll();

                    req.requestMatchers(HttpMethod.POST, "/users/register").permitAll();


                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
                    req.requestMatchers("/connect").permitAll();
                    req.requestMatchers("/topic", "/topic/**", "/ws").permitAll();
                    req.anyRequest().authenticated();
                })
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}