package com.danila.score.service;

import com.danila.score.model.User;
import com.danila.score.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ScoreService {
    private final UserRepository userRepository;

    public ScoreService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Float> getUserScore(String login) {
        return userRepository.findByLogin(login)
                .map(User::getScore)
                .defaultIfEmpty(1.0f); // Если пользователь не найден, возвращаем "хороший" score
    }
}
