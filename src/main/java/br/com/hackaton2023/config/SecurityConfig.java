package br.com.hackaton2023.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf( csrf-> csrf.disable() )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( requests -> requests
                        .requestMatchers("/api/pix/chave", "/api/pix/chave/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pix/chave").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pix/autenticacao/usuario", "/api/pix/autenticacao/instituicao").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/pix/transacoes/pagamento",
                                "/api/pix/transacoes/transferencia",
                                "/api/pix/transacoes/{id}",
                                "/api/pix/transacoes/historico/{usuarioId}",
                                "/api/pix/transacoes/status/{id}"
                                ).permitAll()
                        .anyRequest().authenticated()
                ).formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
