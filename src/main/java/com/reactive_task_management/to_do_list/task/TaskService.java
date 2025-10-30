package com.reactive_task_management.to_do_list.task;

import com.reactive_task_management.to_do_list.exception.task.TaskNotFoundException;
import com.reactive_task_management.to_do_list.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class TaskService {

    private final TaskRepository repository;
    private final UserFacade userFacade;
    private final ReactiveMongoTemplate template;

    Mono<TaskResponse> saveTask(TaskRequest request) {

        final String userId = request.userId();
        // chaining
        return userFacade.getUserById(userId)
                .flatMap(_ -> createTask(request))
                .flatMap(this::saveTask)
                .map(TaskResponse::fromTask);
    }

    Mono<Void> updateTask(String id, TaskRequest request) {
        // chaining
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)))
                .flatMap(existing -> userFacade.getUserById(request.userId()).flatMap(_ -> updateTask(existing, request)))
                .then();
    }

    Mono<Void> deleteTaskById(String id) {
        return repository.deleteById(id);
    }

    Mono<TaskResponse> getTaskById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)))
                .map(TaskResponse::fromTask);
    }

    Flux<TaskResponse> getAllTasks(int page, int size) {
        // Needed to use template because repo.findAll() method doesn't support pagination
        final var query = new Query()
                .skip((long) page * size)
                .limit(size);

        return template.find(query, Task.class).map(TaskResponse::fromTask);
    }

    Flux<TaskResponse> getAllUserTasks(String userId) {
        return repository.findAllByUserId(userId).map(TaskResponse::fromTask);
    }

    private Mono<Task> createTask(TaskRequest request) {
        return Mono.just(Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .userId(request.userId())
                .build());
    }

    private Mono<Task> saveTask(Task task) {
        return repository.save(task);
    }

    private Mono<Task> updateTask(Task task, TaskRequest request) {
        task.updateTaskDetails(request.title(), request.description(), request.status(), request.userId());
        return Mono.just(task);
    }
}
