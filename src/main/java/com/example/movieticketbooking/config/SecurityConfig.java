package com.example.movieticketbooking.config;

import com.example.movieticketbooking.security.JwtAuthenticationFilter;
import com.example.movieticketbooking.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup", "/api/auth/signin").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies", "/api/movies/{id}", "/api/theatres", "/api/theatres/{id}", "/api/shows", "/api/shows/{id}", "/api/shows/movie/{movieId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/movies", "/api/theatres", "/api/shows").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/movies/**", "/api/theatres/**", "/api/shows/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/**", "/api/theatres/**", "/api/shows/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/bookings").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/{id}/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payments/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/bookings").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/bookings/{id}", "/api/bookings/my-bookings").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/{id}/cancel").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/payments").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/payments/{id}", "/api/payments/{id}/status").hasRole("CUSTOMER")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:5174",
                "http://127.0.0.1:5173",
                "http://127.0.0.1:5174"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
