package com.myfinbank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injects CustomerUserDetailsService to fetch customer authentication details
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    // Configures BCrypt password encoder for encrypting customer passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Provides AuthenticationManager bean used for authenticating users
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configures security rules, login handling, and logout behavior
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth
        	    .requestMatchers("/customer/register", "/customer/login", "/css/**", "/js/**","/api/**", "/customer/loans/**").permitAll()
        	    .requestMatchers("/customer/dashboard").authenticated()  
        	    .anyRequest().authenticated()
        	)

            .formLogin(form -> form
            	    .loginPage("/customer/login")
            	    .loginProcessingUrl("/customer/login")
            	    .usernameParameter("email")
            	    .passwordParameter("password")
            	    .successHandler((request, response, authentication) -> {

            	        String email = authentication.getName();

            	        // Fetch customer and store in session
            	        com.myfinbank.entity.Customer customer =
            	                customerUserDetailsService.getCustomerByEmail(email);

            	        request.getSession().setAttribute("loggedCustomer", customer);

            	        response.sendRedirect("/customer/dashboard");
            	    })
            	    .failureUrl("/customer/login?error=true")
            	    .permitAll()
            	)

            .logout(logout -> logout
                .logoutUrl("/customer/logout")
                .logoutSuccessUrl("/customer/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
