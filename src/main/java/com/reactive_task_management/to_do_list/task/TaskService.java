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

    Mono<TaskResponseDTO> saveTask(TaskRequestDTO dto) {

        final var taskToBeSaved = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(dto.status())
                .build();

        return repository.save(taskToBeSaved).map(TaskResponseDTO::fromTask);
    }

    Mono<Void> updateTask(String id, TaskRequestDTO dto) {
        return repository.findById(id).flatMap(existing -> {
            existing.updateTaskDetails(dto.title(), dto.description(), dto.status());
            return repository.save(existing);
        }).then();
    }

    Mono<Void> deleteTaskById(String id) {
        return repository.deleteById(id);
    }

    Mono<TaskResponseDTO> getTaskById(String id) {
        return repository.findById(id).map(TaskResponseDTO::fromTask);
    }

    Flux<TaskResponseDTO> getAllTasks() {
        return repository.findAll().map(TaskResponseDTO::fromTask);
    }
}
