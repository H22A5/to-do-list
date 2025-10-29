package com.reactive_task_management.to_do_list.exception;

public record ErrorResponse(String timestamp,
                            int status,
                            String message,
                            String path) {

    public ErrorResponse() {
        this(null, 0, null, null);
    }
}
