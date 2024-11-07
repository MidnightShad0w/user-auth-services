package com.danila.auth.grpcconfig;

import com.danila.auth.service.GrpcAuthServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcServerConfig {

    @Bean
    public CommandLineRunner grpcServer(GrpcAuthServiceImpl grpcAuthServiceImpl) {
        return args -> {
            Server server = ServerBuilder.forPort(9090)
                    .addService(grpcAuthServiceImpl)
                    .build()
                    .start();
            log.info("gRPC Auth Server started on port 9090");
            server.awaitTermination();
        };
    }
}
