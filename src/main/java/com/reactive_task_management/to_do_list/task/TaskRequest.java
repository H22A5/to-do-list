package com.reactive_task_management.to_do_list.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

record TaskRequest(
        @Schema(example = "Change the tires") @NotBlank(message = "Title cannot be blank.") String title,
        @Schema(example = "Tires should be changed") String description,
        @Schema(example = "CREATED/IN_PROGRESS/DONE") TaskStatus status,
        @Schema(example = "64f9b8e2c17a3a00123d4ef5") @NotBlank(message = "User id cannot be blank.") String userId) {
}
