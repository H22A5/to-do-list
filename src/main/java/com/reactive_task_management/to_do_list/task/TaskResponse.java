package com.reactive_task_management.to_do_list.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

record TaskResponse(
        @Schema(example = "64f9c22a3c4b2a1d5f6e8b09") String id,
        @Schema(example = "Change the tires") String title,
        @Schema(example = "Tires should be changed") String description,
        @Schema(example = "CREATED/IN_PROGRESS/DONE") TaskStatus status,
        @Schema(example = "2025, 10, 30, 17, 54, 47, 891000000") LocalDateTime creationDate,
        @Schema(example = "64f9b8e2c17a3a00123d4ef5") String userId) {

    static TaskResponse fromTask(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreationDate(), task.getUserId());
    }
}
