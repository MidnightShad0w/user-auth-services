package com.danila.composition.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
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
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Float>>() {})
                .map(response -> {
                    Float score = response.get("score");
                    log.warn("Получен ответ от score_service: " + score);
                    return score;
                })
                .onErrorResume(e -> {
                    log.error("Ошибка при вызове score_service для login: " + login, e);
                    return Mono.error(e);
                });
    }

    public Mono<Boolean> authorize(String login, String password) {
        log.warn("Проверка авторизации для логина: " + login);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);

        return webClientBuilder.build()
                .post()
                .uri(authServiceUrl + "/auth/login")
                .bodyValue(credentials)
                .retrieve()
                .bodyToMono(Map.class) // Изменено на Map
                .map(response -> (Boolean) response.get("isAuthorized")) // Извлечение значения
                .doOnNext(isAuthorized -> log.info("Ответ от сервиса авторизации: " + isAuthorized))
                .doOnError(error -> log.error("Ошибка при авторизации: ", error))
                .onErrorReturn(false);
    }
}
