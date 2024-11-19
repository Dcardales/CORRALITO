package com.tecno.corralito.security;

import com.tecno.corralito.security.filter.JwtTokenValidator;
import com.tecno.corralito.services.usuarios.auth.AuthServiceImpl;
import com.tecno.corralito.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                    http.requestMatchers("/corralito/v1/auth/**").permitAll();

                    // Swagger público
                    http.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui-custom.html").permitAll();

                    // Permitir todos los métodos GET excepto los de administradores
                    http.requestMatchers(HttpMethod.GET, "/corralito/v1/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/corralito/v1/administradores/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.GET, "/corralito/v1/turistas/**").hasRole("TURISTA");
                    http.requestMatchers(HttpMethod.GET, "/corralito/v1/comercio/**").hasRole("COMERCIO");
                    http.requestMatchers(HttpMethod.GET, "/corralito/v1/ente-regulador/**").hasRole("ENTEREGULADOR");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/administradores/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/administradores/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/administradores/**").hasRole("ADMIN");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/turistas/**").hasRole("TURISTA");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/turistas/**").hasRole("TURISTA");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/turistas/**").hasRole("TURISTA");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/ente-regulador/**").hasRole("ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/ente-regulador/**").hasRole("ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/ente-regulador/**").hasRole("ENTEREGULADOR");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/comercio/**").hasRole("COMERCIO");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/comercio/**").hasRole("COMERCIO");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/comercio/**").hasRole("COMERCIO");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/categorias/**").hasAnyRole("ADMIN", "ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/categorias/**").hasAnyRole("ADMIN", "ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/categorias/**").hasAnyRole("ADMIN", "ENTEREGULADOR");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/zonas/**").hasAnyRole("ADMIN", "ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/zonas/**").hasAnyRole("ADMIN", "ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/zonas/**").hasAnyRole("ADMIN", "ENTEREGULADOR");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/productos/**").hasAnyRole("ADMIN", "ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/productos/**").hasAnyRole("ADMIN", "ENTEREGULADOR");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/productos/**").hasAnyRole("ADMIN", "ENTEREGULADOR");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/productos-especificos/**").hasAnyRole("ADMIN", "COMERCIO");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/productos-especificos/**").hasAnyRole("ADMIN", "COMERCIO");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/productos-especificos/**").hasAnyRole("ADMIN", "COMERCIO");

                    http.requestMatchers(HttpMethod.POST, "/corralito/v1/comentarios/**").hasAnyRole("ADMIN", "TURISTA");
                    http.requestMatchers(HttpMethod.PUT, "/corralito/v1/comentarios/**").hasAnyRole("ADMIN", "TURISTA");
                    http.requestMatchers(HttpMethod.DELETE, "/corralito/v1/comentarios/**").hasAnyRole("ADMIN", "TURISTA");

                    // Denegar todos los demás accesos
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

