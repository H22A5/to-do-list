package com.reactive_task_management.to_do_list.task;

import com.reactive_task_management.to_do_list.exception.TaskNotFoundException;
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

        String userId = request.userId();
        userFacade.validateUserExists(userId).then();

        final var taskToBeSaved = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .build();

        return repository.save(taskToBeSaved).map(TaskResponse::fromTask);
    }

    Mono<Void> updateTask(String id, TaskRequest dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)))
                .flatMap(existing -> {
                    existing.updateTaskDetails(dto.title(), dto.description(), dto.status());
                    return repository.save(existing);
                }).then();
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
}
