package com.danila.score.grpcconfig;

import com.danila.score.service.GrpcScoreServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcServerConfig {

    @Bean
    public CommandLineRunner grpcServer(GrpcScoreServiceImpl grpcScoreServiceImpl) {
        return args -> {
            Server server = ServerBuilder.forPort(9091)
                    .addService(grpcScoreServiceImpl)
                    .addService(ProtoReflectionService.newInstance())
                    .build()
                    .start();
            log.info("gRPC Score Server started on port 9091");
            server.awaitTermination();
        };
    }
}
