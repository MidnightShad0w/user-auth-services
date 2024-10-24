package com.danila.score.controller;

import com.danila.score.service.ScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/score")
    public Mono<ResponseEntity<Float>> getScore(@RequestParam String login) {
        return scoreService.getUserScore(login)
                .map(score -> ResponseEntity.ok(score))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
