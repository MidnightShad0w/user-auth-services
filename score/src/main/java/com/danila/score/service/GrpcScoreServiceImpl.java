package com.danila.score.service;

import com.danila.score.ScoreRequest;
import com.danila.score.ScoreResponse;
import com.danila.score.ScoreServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GrpcScoreServiceImpl extends ScoreServiceGrpc.ScoreServiceImplBase {

    private final ScoreService scoreService;

    @Autowired
    public GrpcScoreServiceImpl(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Override
    public void getUserScore(ScoreRequest request, StreamObserver<ScoreResponse> responseObserver) {
        // Асинхронно получаем score пользователя
        Mono<Float> scoreMono = scoreService.getUserScore(request.getLogin());

        // Подписываемся на результат score
        scoreMono.subscribe(score -> {
            // Создаем ответ для клиента
            ScoreResponse response = ScoreResponse.newBuilder()
                    .setScore(score)
                    .build();

            // Отправляем ответ и завершаем поток
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }, error -> {
            // Обработка ошибок
            responseObserver.onError(error);
        });
    }
}
