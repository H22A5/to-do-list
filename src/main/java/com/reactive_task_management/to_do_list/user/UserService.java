package com.reactive_task_management.to_do_list.user;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class UserService {

    private final UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }

    Mono<UserDTO> getUserById(String id) {
        return repository.findById(id).map(UserDTO::fromUser);
    }
}
