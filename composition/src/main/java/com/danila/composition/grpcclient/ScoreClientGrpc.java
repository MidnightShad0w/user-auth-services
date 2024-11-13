package com.danila.composition.grpcclient;

import com.danila.score.ScoreRequest;
import com.danila.score.ScoreResponse;
import com.danila.score.ScoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScoreClientGrpc {
    private final ScoreServiceGrpc.ScoreServiceBlockingStub scoreStub;
    private final ManagedChannel channel;

    public ScoreClientGrpc(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.scoreStub = ScoreServiceGrpc.newBlockingStub(channel);
    }

    public float getUserScore(String login) {
        try {
            log.info("Отправка gRPC запроса в ScoreService для login: {}", login);
            ScoreRequest request = ScoreRequest.newBuilder()
                    .setLogin(login)
                    .build();
            ScoreResponse response = scoreStub.getUserScore(request);
            log.info("Получен ответ от ScoreService: {}", response);
            return response.getScore();
        } catch (io.grpc.StatusRuntimeException e) {
            log.error("gRPC ошибка: статус={}, описание={}, причина={}",
                    e.getStatus().getCode(),
                    e.getStatus().getDescription(),
                    e.getCause());
            throw e;
        }
    }

    public void shutdown() {
        log.info("Закрытие gRPC канала");
        channel.shutdown();
    }
}


