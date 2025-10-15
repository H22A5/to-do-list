package com.reactive_task_management.to_do_list.task;

import com.reactive_task_management.to_do_list.user.UserFacade;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class TaskService {

    private final TaskRepository repository;
    private final UserFacade userFacade;

    public TaskService(TaskRepository repository, UserFacade userFacade) {
        this.repository = repository;
        this.userFacade = userFacade;
    }

    Mono<TaskResponse> saveTask(TaskRequest dto) {

        final var taskToBeSaved = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(dto.status())
                .build();

        return repository.save(taskToBeSaved).map(TaskResponse::fromTask);
    }

    Mono<Void> updateTask(String id, TaskRequest dto) {
        return repository.findById(id).flatMap(existing -> {
            existing.updateTaskDetails(dto.title(), dto.description(), dto.status());
            return repository.save(existing);
        }).then();
    }

    Mono<Void> deleteTaskById(String id) {
        return repository.deleteById(id);
    }

    Mono<TaskResponse> getTaskById(String id) {
        return repository.findById(id).map(TaskResponse::fromTask);
    }

    Flux<TaskResponse> getAllTasks() {
        return repository.findAll().map(TaskResponse::fromTask);
    }
}
