package com.oliveira.taskstodo.configs;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //@EnableGlobalMethodSecurity(prePostEnabled = true) / deprecat 
public class SecurityConfig {


        //public system rout 
        private static final String[] PUBLIC_MATCHERS = { 
            "/"
        };
        
        private static final String[] PUBLIC_MATCHERS_POST = {
            "/user",
            "/login"
        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            //http.cors().and().csrf().disable();

            http.cors(cors -> cors.disable());

            http.csrf(csrf -> csrf.disable());
            
            //.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
            //.antMatchers(PUBLIC_MATCHERS).permitAll()
            http.authorizeHttpRequests( authz -> authz
                .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated());


            //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            return http.build();
        }

        @Bean // Ensure that there is no cor's problen  
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
            configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

        @Bean // Use to Encrypt
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

}
