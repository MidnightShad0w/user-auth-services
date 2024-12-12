package com.danila.composition.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Authorization API")
                        .version("1.0")
                        .description("API for users authorization via REST + gRPC")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
