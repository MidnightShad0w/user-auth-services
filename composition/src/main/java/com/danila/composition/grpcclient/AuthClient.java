package com.danila.composition.grpcclient;

import com.danila.auth.AuthRequest;
import com.danila.auth.AuthResponse;
import com.danila.auth.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AuthClient {

    private final AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public AuthClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        authStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public boolean authorize(String login, String password) {
        AuthRequest request = AuthRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build();
        AuthResponse response = authStub.authorize(request);
        return response.getIsAuthorized();
    }
}
