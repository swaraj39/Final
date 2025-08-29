package com.pack.demo.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.pack.demo.Services.SecurityCustomUserDetail;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetail userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    // Allow public access to the login page and the welcome page
                    auth.requestMatchers( "/","/welcome","/demo","/signup", "/signups", "/resend", "/codewala",
                    "/forgot", "/forgotlink", "/setpassword", "/conform", "/us", "/ceo").permitAll()
                    .requestMatchers("/images/**", "/css/**", "/js/**").permitAll();
                    auth.requestMatchers("/ques/add").hasRole("ADMIN");
                    auth.anyRequest().authenticated(); // Require authentication for all other requests
                })
                .formLogin(form -> form
                        .loginPage("/signup") // Custom login page
                        .loginProcessingUrl("/loginn") // URL that Spring Security will handle
                        .defaultSuccessUrl("/home", true) // Redirect to /new on successful login
                        .failureUrl("/signup?error=true") // Redirect to signup page on failure
                        .usernameParameter("id") // Name of the username field in the form
                        .passwordParameter("password") // Name of the password field in the form
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL for logging out
                        .logoutSuccessUrl("/welcome") // Redirect to /welcome after logout
                )
                .rememberMe(remember -> remember
                        //.key("uniqueAndSecret") // Secret key to sign token
                        .tokenValiditySeconds(60 * 60) // 1 day
                        .rememberMeParameter("remember-me") // name of the checkbox
                        .userDetailsService(userDetailsService)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/denied")
                );

        return http.build();
    }

}
