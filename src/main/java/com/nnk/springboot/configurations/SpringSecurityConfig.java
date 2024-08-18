package com.nnk.springboot.configurations;

import com.nnk.springboot.services.CustomUserDetailsService;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final CustomUserDetailsService usersService;

    public SpringSecurityConfig(CustomUserDetailsService usersService) {
        this.usersService = usersService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Returns a SecurityFilterChain for the given HttpSecurity configuration.
     * Authentication required for all requests + admin role for requests about users
     *
     * @param http the HttpSecurity configuration to build the SecurityFilterChain from
     * @return the SecurityFilterChain for the given HttpSecurity configuration
     * @ if an error occurs while building the SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth
                        .requestMatchers("/user/**","/admin/home").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .defaultSuccessUrl("/app/home", true))
                .logout(logout -> logout.clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

    /**
     * Creates and returns an AuthenticationManager for the given HttpSecurity configuration.
     *
     * @param http the HttpSecurity configuration from which to obtain the shared AuthenticationManagerBuilder
     * @param bCryptPasswordEncoder the BCryptPasswordEncoder to use for password encoding
     * @return the created AuthenticationManager
     * @ if an error occurs while building the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Returns a ServletContextInitializer that sets the session timeout at 10 minutes for the servlet context.
     *
     * @return the ServletContextInitializer that sets the session timeout
     */
    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> servletContext.setSessionTimeout(10);
    }
}
