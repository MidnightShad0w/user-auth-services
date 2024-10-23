package com.danila.composition.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CompositionService {
    private final WebClient.Builder webClientBuilder;

    @Value("${score.service.url}")
    private String scoreServiceUrl;

    public CompositionService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Float> getScoreForLogin(String login) {
        return webClientBuilder.build()
                .get()
                .uri(scoreServiceUrl + "/score?login" + login)
                .retrieve()
                .bodyToMono(Float.class);
    }

    public Mono<Boolean> authorize(String login, String password) {
        // todo:отправка запроса в auth
        return Mono.just(true);
    }
}
