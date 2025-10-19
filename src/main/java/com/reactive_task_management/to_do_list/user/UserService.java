package com.reactive_task_management.to_do_list.user;

import com.reactive_task_management.to_do_list.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class UserService {

    private final UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }

    Mono<Void> validateUserExists(String id) {
        return repository.existsById(id)
                .flatMap(exists -> exists ? Mono.empty() : Mono.error(new UserNotFoundException(id)));
    }

    Mono<UserResponse> saveUser(UserRequest request) {

        final var user = User.builder()
                .username(request.username())
                .build();

        return repository.save(user).map(UserResponse::fromUser);
    }
}
