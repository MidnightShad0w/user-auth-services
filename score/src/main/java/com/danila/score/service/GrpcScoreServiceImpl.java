package com.danila.score.service;

import com.danila.score.ScoreRequest;
import com.danila.score.ScoreResponse;
import com.danila.score.ScoreServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GrpcScoreServiceImpl extends ScoreServiceGrpc.ScoreServiceImplBase {

    private final ScoreService scoreService;

    @Autowired
    public GrpcScoreServiceImpl(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Override
    public void getUserScore(ScoreRequest request, StreamObserver<ScoreResponse> responseObserver) {
        try {
            log.info("Получен запрос для login: {}", request.getLogin());
            Mono<Float> scoreMono = scoreService.getUserScore(request.getLogin());

            scoreMono.subscribe(score -> {
                log.info("Найден score: {}", score);
                ScoreResponse response = ScoreResponse.newBuilder()
                        .setScore(score)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }, error -> {
                log.error("Ошибка при обработке запроса: ", error);
                responseObserver.onError(error);
            });
        } catch (Exception e) {
            log.error("Необработанное исключение: ", e);
            responseObserver.onError(e);
        }
    }
}
