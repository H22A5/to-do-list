package com.reactive_task_management.to_do_list.user;

import com.reactive_task_management.to_do_list.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserRepository repository;

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
