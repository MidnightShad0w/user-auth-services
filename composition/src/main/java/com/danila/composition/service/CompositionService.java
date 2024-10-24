package com.danila.composition.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CompositionService {
    private final WebClient.Builder webClientBuilder;
    private final Dotenv dotenv = Dotenv.load();
    private final float scoreThreshold;

    @Value("${score.service.url}")
    private String scoreServiceUrl;
    @Value("${auth.service.url}")
    private String authServiceUrl;

    public CompositionService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.scoreThreshold = Float.parseFloat(dotenv.get("USER_SCORE_THRESHOLD"));
    }

    public float getScoreThreshold() {
        return scoreThreshold;
    }

    public Mono<Float> getScoreForLogin(String login) {
        log.warn("Отправка запроса в score...");
        return webClientBuilder.build()
                .get()
                .uri(scoreServiceUrl + "/score?login=" + login)
                .retrieve()
                .bodyToMono(Float.class)
                .onErrorResume(e -> {
                    log.error("Error calling score service for login: " + login, e);
                    return Mono.error(e);
                });
    }


    public Mono<Boolean> authorize(String login, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);

        return webClientBuilder.build()
                .post()
                .uri(authServiceUrl + "/auth/login")
                .bodyValue(credentials)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);
    }
}
