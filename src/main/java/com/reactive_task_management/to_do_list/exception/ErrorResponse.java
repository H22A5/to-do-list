package com.reactive_task_management.to_do_list.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ErrorResponse (
        @Schema(example = "2025-10-30T17:01:31.935881100Z") String timestamp,
        @Schema(example = "404/400") int status,
        @Schema(example = "Record not found./Record field is blank.") List<String> messages,
        @Schema(example = "/tasks") String path) {

    public static ErrorResponse empty() {
        return new ErrorResponse(null, 0, List.of(), null);
    }
}
