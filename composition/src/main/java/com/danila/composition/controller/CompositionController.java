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

    @PostMapping("/authorize")
    public Mono<ResponseEntity<String>> authorizeUser(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");
        float scoreThreshold = compositionService.getScoreThreshold();

        return compositionService.getScoreForLogin(login)
                .flatMap(score -> {
                    if (score < scoreThreshold) {
                        log.warn("Not enough score... its only - " + score + " but u need - " + scoreThreshold);
                        return Mono.just(ResponseEntity.status(403).body("Authorization denied due to low score"));
                    } else {
                        return compositionService.authorize(login, password)
                                .flatMap(authResult -> {
                                    if (authResult) {
                                        log.info("Authorization on score passing -- login:" + login + " and password:" + password + "--- score=" + score);
                                        return Mono.just(ResponseEntity.ok("Authorization successful"));
                                    } else {
                                        return Mono.just(ResponseEntity.status(403).body("Invalid credentials"));
                                    }
                                });
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error calling score service", e);
                    // Если сервис score отвечает ошибкой, считаем, что score "хороший"
                    return compositionService.authorize(login, password)
                            .flatMap(authResult -> {
                                if (authResult) {
                                    log.error("Authorization on error -- login:" + login + " and password:" + password);
                                    return Mono.just(ResponseEntity.ok("Authorization successful"));
                                } else {
                                    return Mono.just(ResponseEntity.status(403).body("Invalid credentials"));
                                }
                            });
                });
    }
}
