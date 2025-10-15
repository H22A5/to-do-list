package com.reactive_task_management.to_do_list.task;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("/tasks")
class TaskController {

    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    Flux<TaskResponseDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<TaskResponseDTO>> getTaskById(@PathVariable("id") String id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.status(OK).body(task))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    Mono<TaskResponseDTO> saveTask(@Valid @RequestBody TaskRequestDTO dto) {
        return taskService.saveTask(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> updateTask(
            @PathVariable("id") String id,
            @Valid @RequestBody TaskRequestDTO dto) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> deleteTaskById(@PathVariable("id") String id) {
        return taskService.deleteTaskById(id).map(t -> ResponseEntity.noContent().build());
    }
}
