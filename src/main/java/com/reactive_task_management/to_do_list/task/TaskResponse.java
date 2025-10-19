package com.reactive_task_management.to_do_list.task;

import java.time.LocalDateTime;

record TaskResponse(String id,
                    String title,
                    String description,
                    TaskStatus status,
                    LocalDateTime creationDate,
                    String userId) {

    static TaskResponse fromTask(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreationDate(), task.getUserId());
    }
}
