package com.reactive_task_management.to_do_list.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String BASE_MESSAGE = "User[id=%s] not found.";

    public UserNotFoundException(String id) {
        super(BASE_MESSAGE.formatted(id));
    }
}
