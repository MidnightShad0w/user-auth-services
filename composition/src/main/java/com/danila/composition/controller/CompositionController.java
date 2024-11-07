package com.danila.composition.controller;

import com.danila.composition.service.CompositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/composition")
public class CompositionController {

    private final CompositionService compositionService;

    public CompositionController(CompositionService compositionService) {
        this.compositionService = compositionService;
    }

    @PostMapping("/authorize-grpc")
    public Mono<ResponseEntity<Map<String, String>>> authorizeUserGrpc(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");
        float scoreThreshold = compositionService.getScoreThreshold();

        return compositionService.getScoreForLoginGrpc(login)
                .flatMap(score -> {
                    if (score < scoreThreshold) {
                        log.warn("Недостаточно очков: " + score + ", требуется: " + scoreThreshold);
                        return Mono.just(ResponseEntity.status(403)
                                .body(Map.of("message", "Authorization denied due to low score")));
                    } else {
                        return compositionService.authorizeGrpc(login, password)
                                .flatMap(authResult -> {
                                    if (authResult) {
                                        log.info("Авторизация успешна для логина: " + login);
                                        return Mono.just(ResponseEntity.ok(Map.of("message", "Authorization successful")));
                                    } else {
                                        log.warn("Авторизация не удалась для логина: " + login);
                                        return Mono.just(ResponseEntity.status(403)
                                                .body(Map.of("message", "Invalid credentials")));
                                    }
                                })
                                .doOnError(e -> log.error("Ошибка при авторизации для логина: " + login, e));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Ошибка вызова gRPC сервисов", e);
                    return Mono.just(ResponseEntity.status(500)
                            .body(Map.of("message", "Error during authorization")));
                });
    }

    @PostMapping("/authorize")
    public Mono<ResponseEntity<Map<String, String>>> authorizeUser(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");
        float scoreThreshold = compositionService.getScoreThreshold();

        return compositionService.getScoreForLogin(login)
                .flatMap(score -> {
                    if (score < scoreThreshold) {
                        log.warn("Недостаточно очков: " + score + ", требуется: " + scoreThreshold);
                        return Mono.just(ResponseEntity.status(403)
                                .body(Map.of("message", "Authorization denied due to low score")));
                    } else {
                        return compositionService.authorize(login, password)
                                .flatMap(authResult -> {
                                    if (authResult) {
                                        log.info("Авторизация успешна для логина: " + login);
                                        return Mono.just(ResponseEntity.ok(Map.of("message", "Authorization successful")));
                                    } else {
                                        log.warn("Авторизация не удалась для логина: " + login);
                                        return Mono.just(ResponseEntity.status(403)
                                                .body(Map.of("message", "Invalid credentials")));
                                    }
                                })
                                .doOnError(e -> log.error("Ошибка при авторизации для логина: " + login, e));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Ошибка вызова score сервиса", e);
                    return compositionService.authorize(login, password)
                            .flatMap(authResult -> {
                                if (authResult) {
                                    log.info("Авторизация успешна для логина: " + login + " при ошибке score");
                                    return Mono.just(ResponseEntity.ok(Map.of("message", "Authorization successful")));
                                } else {
                                    return Mono.just(ResponseEntity.status(403)
                                            .body(Map.of("message", "Invalid credentials")));
                                }
                            });
                });
    }
}

