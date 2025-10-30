package com.reactive_task_management.to_do_list.exception.task;

public class TaskNotFoundException extends RuntimeException {

    private static final String BASE_MESSAGE = "Task[id=%s] not found.";

    public TaskNotFoundException(String id) {
        super(BASE_MESSAGE.formatted(id));
    }
}
