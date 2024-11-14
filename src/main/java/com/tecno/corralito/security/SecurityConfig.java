package com.tecno.corralito.security;


import com.tecno.corralito.security.filter.JwtTokenValidator;
import com.tecno.corralito.services.usuarios.auth.AuthServiceImpl;
import com.tecno.corralito.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);  // Permitir credenciales
                    config.addAllowedOrigin("http://localhost:4200");
                    config.addAllowedHeader("*");  // Permitir todos los encabezados
                    config.addAllowedMethod("*");  // Permitir todos los métodos
                    return config;
                }))  // Configuración CORS directamente aquí
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(http -> {
                    // Endpoints públicos
                    http.requestMatchers("/auth/**").permitAll();

                    // Swagger público
                    http.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui-custom.html").permitAll();

                    // Endpoints de administrador (protegidos)
                    http.requestMatchers("/api/administradores/**").hasAnyRole("ADMIN");

                    http.requestMatchers("/api/turistas/**").hasAnyRole("TURISTA");

                    // Endpoints de Categoría (protegidos)
                    http.requestMatchers("/api/categorias/**").hasAnyRole("ADMIN", "ENTEREGULADOR");

                    // Endpoints de Zonas (protegidos)
                    http.requestMatchers("/api/zonas/**").hasAnyRole("ADMIN", "ENTEREGULADOR");

                    // Endpoints de Producto (protegidos)
                    http.requestMatchers("/api/productos/**").hasAnyRole("ADMIN", "ENTEREGULADOR");

                    // Endpoints de Producto Especifico (protegidos)
                    http.requestMatchers("/api/productos-especificos/**").hasAnyRole("ADMIN", "COMERCIO");

                    // Endpoints de Comentarios (protegidos)
                    http.requestMatchers("/api/comentarios/**").hasAnyRole("ADMIN", "TURISTA");

                    // Denegar todos los demás
                    http.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(AuthServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

