package com.reactive_task_management.to_do_list.task;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

record TaskRequestDTO(@NotBlank String title,
                      String description,
                      TaskStatus status,
                      @NotBlank String userId) {
}
