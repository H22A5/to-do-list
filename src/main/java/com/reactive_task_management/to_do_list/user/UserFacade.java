package com.reactive_task_management.to_do_list.user;

import reactor.core.publisher.Mono;

public interface UserFacade {

    Mono<UserDTO> getUserById(String id);
}
