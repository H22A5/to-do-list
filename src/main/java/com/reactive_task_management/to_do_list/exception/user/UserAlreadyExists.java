package com.reactive_task_management.to_do_list.exception.user;

public class UserAlreadyExists extends RuntimeException {

    private static final String BASE_MESSAGE = "User with [username=%s] already exists.";

    public UserAlreadyExists(String username) {
        super(BASE_MESSAGE.formatted(username));
    }
}
