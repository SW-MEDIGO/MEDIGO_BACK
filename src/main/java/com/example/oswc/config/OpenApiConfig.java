package com.example.oswc.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(
            new Info().title("OSWC Backend API")
                      .version("0.0.1")
                      .description("OpenAPI docs for OSWC (MongoDB)")
        );
    }
}
