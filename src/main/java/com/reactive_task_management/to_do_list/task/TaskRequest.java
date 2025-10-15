package com.reactive_task_management.to_do_list.task;

import jakarta.validation.constraints.NotBlank;

record TaskRequest(@NotBlank String title,
                   String description,
                   TaskStatus status,
                   @NotBlank String userId) {
}
