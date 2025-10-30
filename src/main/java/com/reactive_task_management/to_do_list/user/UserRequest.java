package com.reactive_task_management.to_do_list.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

record UserRequest(
        @Schema(example = "H22A5") @NotBlank(message = "Username cannot be blank.") String username) {
}
