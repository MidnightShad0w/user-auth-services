package com.danila.composition.config;

import com.danila.composition.grpcclient.AuthClient;
import com.danila.composition.grpcclient.ScoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public AuthClient authClient() {
        return new AuthClient("localhost", 9090);
    }

    @Bean
    public ScoreClient scoreClient() {
        return new ScoreClient("localhost", 9091);
    }
}
