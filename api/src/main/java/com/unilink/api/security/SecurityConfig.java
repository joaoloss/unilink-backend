package com.unilink.api.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.unilink.api.enums.UserRole;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                // .requestMatchers(HttpMethod.POST, "/api/tags").authenticated()
                // .requestMatchers(HttpMethod.PUT, "/api/projects/**").authenticated() // more verifications done in service layer
                // .requestMatchers(HttpMethod.GET, "/api/centers", "/api/centers/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/api/tags", "/api/tags/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/api/projects", "/api/projects/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/api/test").permitAll()
                // .requestMatchers(HttpMethod.GET, "/api/users").hasRole(UserRole.SUPER_ADMIN.getValue())                
                // .requestMatchers(HttpMethod.POST).hasRole(UserRole.SUPER_ADMIN.getValue())
                // .requestMatchers(HttpMethod.PUT).hasRole(UserRole.SUPER_ADMIN.getValue())

                // .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api-docs/**", "/api-docs", "/swagger-ui.html/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(authProvider);
    }
}
