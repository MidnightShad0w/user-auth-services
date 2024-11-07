package com.danila.composition.grpcclient;

import com.danila.score.ScoreRequest;
import com.danila.score.ScoreResponse;
import com.danila.score.ScoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ScoreClient {

    private final ScoreServiceGrpc.ScoreServiceBlockingStub scoreStub;

    public ScoreClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        scoreStub = ScoreServiceGrpc.newBlockingStub(channel);
    }

    public float getUserScore(String login) {
        ScoreRequest request = ScoreRequest.newBuilder()
                .setLogin(login)
                .build();
        ScoreResponse response = scoreStub.getUserScore(request);
        return response.getScore();
    }
}
