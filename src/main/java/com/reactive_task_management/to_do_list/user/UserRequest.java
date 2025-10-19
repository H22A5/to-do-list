package com.reactive_task_management.to_do_list.user;

import jakarta.validation.constraints.NotBlank;

record UserRequest(@NotBlank String username) {
}
