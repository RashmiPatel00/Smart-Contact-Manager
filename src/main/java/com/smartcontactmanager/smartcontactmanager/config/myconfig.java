package com.smartcontactmanager.smartcontactmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class myconfig {

    // Provide your UserDetailsService implementation
    @Bean
    public UserDetailsService userDetailsService() {
        return new USerDetailsServiceImpl(); // Ensure this implementation exists
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signin", "/signup", "/home").permitAll() // Public pages
                .requestMatchers("/user/**").authenticated() // Protected user routes
                .requestMatchers("/admin/**").hasRole("ADMIN") // Admin-specific pages
                .requestMatchers("/passengerPage").hasRole("PASSENGER") // Passenger-specific pages
                .requestMatchers("/driverPage").hasRole("DRIVER") // Driver-specific pages
                .anyRequest().authenticated() // All other requests must be authenticated
            )
            .formLogin(form -> form
                .permitAll()
                .loginPage("/signin")
                .loginProcessingUrl("/dologin")
                .successHandler(successHandler()) // Use the custom success handler
                .failureUrl("/signin?error=true")
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/signin?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Disable CSRF for development (enable for production)
        
        return http.build();
    }

    // Custom AuthenticationSuccessHandler to handle redirection based on role
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().toString();
            if (role.contains("ROLE_PASSENGER")) {
                response.sendRedirect("/user/index"); // Redirect to passenger page
            } else if (role.contains("ROLE_DRIVER")) {
                response.sendRedirect("/driverPage"); // Redirect to driver page
            } else {
                response.sendRedirect("/"); // Default page for other roles
            }
        };
    }
}
