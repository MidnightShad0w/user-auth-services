package com.danila.composition.config;

import com.danila.composition.service.GrpcCompositionServiceImpl;
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
    public CommandLineRunner grpcServer(GrpcCompositionServiceImpl grpcCompositionService) {
        return args -> {
            Server server = ServerBuilder.forPort(9092)
                    .addService(grpcCompositionService)
                    .build()
                    .start();
            log.info("gRPC Auth Server started on port 9092");
            server.awaitTermination();
        };
    }
}
