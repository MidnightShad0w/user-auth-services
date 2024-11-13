package com.danila.composition.config;

import com.danila.composition.grpcclient.AuthClientGrpc;
import com.danila.composition.grpcclient.ScoreClientGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public AuthClientGrpc authClient() {
        return new AuthClientGrpc("localhost", 9090);
    }

    @Bean
    public ScoreClientGrpc scoreClient() {
        return new ScoreClientGrpc("localhost", 9091);
    }
}
