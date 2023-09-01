// configure CORS to applications spring boot
// comunication with frontend

package com.oliveira.taskstodo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("*") 
                .allowedOrigins("http://localhost:5500")
                .allowedHeaders("*")
                .allowedMethods("*");
    }

}
