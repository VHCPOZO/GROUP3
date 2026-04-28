package com.Grupo3.arquiteconvencionales.config;

import com.Grupo3.arquiteconvencionales.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final var usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                    Constantes.ERR_USUARIO_NO_ENCONTRADO + ": " + username));

            return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().replace(Constantes.ROLE_PREFIX, ""))
                .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(Constantes.RUTAS_PUBLICAS.split(","))
                .permitAll()
                // Proteger rutas exclusivas de administrador
                .requestMatchers(Constantes.RUTAS_ADMIN.split(","))
                .hasRole(Constantes.ROLE_ADMIN.replace(Constantes.ROLE_PREFIX, ""))
                // Rutas para usuarios autenticados
                .requestMatchers(Constantes.RUTAS_AUTENTICADOS.split(","))
                .authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/" + Constantes.VIEW_LOGIN)
                .defaultSuccessUrl("/" + Constantes.VIEW_INFO_ARQUITECTURA, true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/" + Constantes.VIEW_LOGIN + "?logout")
                .permitAll()
            );

        return http.build();
    }
}