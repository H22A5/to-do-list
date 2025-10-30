package com.reactive_task_management.to_do_list.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    @Override
    public Mono<UserView> getUserById(String id) {
        return userService.getUserViewById(id);
    }
}
