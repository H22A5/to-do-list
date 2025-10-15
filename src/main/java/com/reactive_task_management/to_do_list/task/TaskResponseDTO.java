package com.reactive_task_management.to_do_list.task;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

record TaskResponseDTO(String id,
                       String title,
                       String description,
                       TaskStatus status,
                       LocalDateTime creationDate) {

    static TaskResponseDTO fromTask(Task task) {
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreationDate());
    }
}
