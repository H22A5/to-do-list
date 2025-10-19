package com.reactive_task_management.to_do_list.user;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    UserFacadeImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Void> validateUserExists(String id) {
        return userService.validateUserExists(id);
    }
}
