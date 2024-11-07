package com.danila.auth.service;

import com.danila.auth.AuthRequest;
import com.danila.auth.AuthResponse;
import com.danila.auth.AuthServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GrpcAuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;

    @Autowired
    public GrpcAuthServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void authorize(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        Mono<Boolean> isAuthorizedMono = authService.authorize(request.getLogin(), request.getPassword());

        isAuthorizedMono.subscribe(isAuthorized -> {
            AuthResponse response = AuthResponse.newBuilder()
                    .setIsAuthorized(isAuthorized)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }, error -> {
            responseObserver.onError(error);
        });
    }
}
