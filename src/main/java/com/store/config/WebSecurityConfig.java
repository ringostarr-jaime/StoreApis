package com.store.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	
	@Value("${client.version}clients/addClient")
	private String urlClient;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(AbstractHttpConfigurer::disable)
	        .cors(cors -> cors.configurationSource(request -> {
	            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
	            // Usa allowedOriginPatterns en lugar de allowedOrigins
	            corsConfig.setAllowedOriginPatterns(java.util.List.of("*")); 
	            corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	            corsConfig.setAllowedHeaders(java.util.List.of("*"));
	            corsConfig.setAllowCredentials(true);
	            corsConfig.addExposedHeader("Authorization");
	            return corsConfig;
	        }))
	        .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(HttpMethod.POST, "/token").permitAll()
	            .requestMatchers(HttpMethod.POST, urlClient).permitAll()
	            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
	            .anyRequest().authenticated()
	        );
	    return http.build();
	}


    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() {
        return new JWTAuthorizationFilter();
    }
}
