package com.danila.composition.config;

import com.danila.composition.grpcclient.AuthClientGrpc;
import com.danila.composition.grpcclient.ScoreClientGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${score.service.url.grpc}")
    private String scoreServiceUrlGrpc;
    @Value("${auth.service.url.grpc}")
    private String authServiceUrlGrpc;
    @Bean
    public AuthClientGrpc authClient() {
        return new AuthClientGrpc(authServiceUrlGrpc, 9090);
    }

    @Bean
    public ScoreClientGrpc scoreClient() {
        return new ScoreClientGrpc(scoreServiceUrlGrpc, 9091);
    }
}
