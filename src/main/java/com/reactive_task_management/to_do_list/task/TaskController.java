package com.reactive_task_management.to_do_list.task;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Flux<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<TaskResponse>> getTaskById(@PathVariable("id") String id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.status(OK).body(task))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    Mono<TaskResponse> saveTask(@Valid @RequestBody Mono<TaskRequest> request) {
        return request.flatMap(taskService::saveTask);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> updateTask(
            @PathVariable("id") String id,
            @Valid @RequestBody TaskRequest dto) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> deleteTaskById(@PathVariable("id") String id) {
        return taskService.deleteTaskById(id);
    }

    @GetMapping("/user/{userId}")
    Flux<TaskResponse> getAllUserTasks(@PathVariable("userId") String userId) {
        return taskService.getAllUserTasks(userId);
    }
}
