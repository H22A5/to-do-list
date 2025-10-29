package com.reactive_task_management.to_do_list.task;

import jakarta.validation.constraints.NotBlank;

record TaskRequest(@NotBlank(message = "Title cannot be blank.") String title,
                   String description,
                   TaskStatus status,
                   @NotBlank(message = "User id cannot be blank.") String userId) {
}
