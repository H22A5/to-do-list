package com.reactive_task_management.to_do_list.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    Mono<UserResponse> saveUser(@Valid @RequestBody Mono<UserRequest> request) {
        return request.flatMap(userService::saveUser);
    }

}
