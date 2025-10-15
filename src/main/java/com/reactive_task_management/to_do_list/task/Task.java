package com.reactive_task_management.to_do_list.task;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.PRIVATE;

@Document("tasks")
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@Builder
@Getter
class Task {

    @Id
    private String id;

    private String title;

    private String description;

    @CreatedDate
    private LocalDateTime creationDate;

    private TaskStatus status;

    private String userId;

    void updateTaskDetails(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
