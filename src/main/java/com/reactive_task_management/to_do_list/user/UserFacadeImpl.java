package com.reactive_task_management.to_do_list.user;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class UserFacadeImpl implements UserFacade {

    private final UserService service;

    UserFacadeImpl(UserService service) {
        this.service = service;
    }

    @Override
    public Mono<UserDTO> getUserById(String id) {
        return service.getUserById(id);
    }
}
