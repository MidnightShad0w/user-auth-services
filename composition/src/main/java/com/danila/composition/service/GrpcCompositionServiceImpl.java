package com.danila.composition.service;

import com.danila.composition.AuthorizeRequest;
import com.danila.composition.AuthorizeResponse;
import com.danila.composition.CompositionServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GrpcCompositionServiceImpl extends CompositionServiceGrpc.CompositionServiceImplBase {

    private final CompositionService compositionService;

    @Autowired
    public GrpcCompositionServiceImpl(CompositionService compositionService) {
        this.compositionService = compositionService;
    }

    @Override
    public void authorizeAndCheckScore(AuthorizeRequest request, StreamObserver<AuthorizeResponse> responseObserver) {
        String login = request.getLogin();
        String password = request.getPassword();
        float scoreThreshold = compositionService.getScoreThreshold();

        compositionService.getScoreForLoginGrpc(login)
                .flatMap(score -> {
                    if (score < scoreThreshold) {
                        return Mono.just(AuthorizeResponse.newBuilder()
                                .setIsAuthorized(false)
                                .setMessage("Authorization denied due to low score")
                                .build());
                    } else {
                        return compositionService.authorizeGrpc(login, password)
                                .map(isAuthorized -> {
                                    if (isAuthorized) {
                                        return AuthorizeResponse.newBuilder()
                                                .setIsAuthorized(true)
                                                .setMessage("Authorization successful")
                                                .build();
                                    } else {
                                        return AuthorizeResponse.newBuilder()
                                                .setIsAuthorized(false)
                                                .setMessage("Invalid credentials")
                                                .build();
                                    }
                                });
                    }
                })
                .subscribe(response -> {
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }, error -> {
                    responseObserver.onError(error);
                });
    }
}
