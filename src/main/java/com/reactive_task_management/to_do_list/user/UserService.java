package com.reactive_task_management.to_do_list.user;

import com.reactive_task_management.to_do_list.exception.user.UserAlreadyExists;
import com.reactive_task_management.to_do_list.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserRepository repository;

    Mono<UserResponse> saveUser(UserRequest request) {
        final var username = request.username();

        return findUserByUsername(username)
                .flatMap(_ -> Mono.error(new UserAlreadyExists(username)))
                .then(createUser(request))
                .flatMap(this::saveUser)
                .map(UserResponse::fromUser);
    }

    public Mono<UserView> getUserViewById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
                .map(UserView::fromUser);
    }

    private Mono<User> createUser(UserRequest request) {
        return Mono.just(User.builder()
                .username(request.username())
                .build());
    }

    private Mono<User> saveUser(User user) {
        return repository.save(user);
    }

    private Mono<User> findUserByUsername(String username) {
        return repository.findByUsername(username);
    }
}
