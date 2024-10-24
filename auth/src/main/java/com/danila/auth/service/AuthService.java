package com.danila.auth.service;

import com.danila.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Boolean> authorize(String login, String password) {
        return userRepository.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .defaultIfEmpty(false); // Если пользователь не найден, возвращаем false
    }
}
