package com.reactive_task_management.to_do_list.task;

import java.time.LocalDateTime;

public record TaskView(String id,
                       String title,
                       String description,
                       LocalDateTime creationDate,
                       TaskStatus status) {
}
