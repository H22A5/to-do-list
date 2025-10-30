package com.reactive_task_management.to_do_list.user;

import io.swagger.v3.oas.annotations.media.Schema;

record UserResponse(
        @Schema(example = "69039857997e8c9998b8c90c") String id,
        @Schema(example = "H22A5") String username) {

    static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }
}
