package com.reactive_task_management.to_do_list.task;

import com.reactive_task_management.to_do_list.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task related operations")
class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Returns all tasks",
            description = "Returns reactive Flux stream of TaskResponse",
            parameters = {
                    @Parameter(name = "page", description = "Number of page"),
                    @Parameter(name = "size", description = "Count of Tasks on page")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Tasks successfully found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
                    )
            }
    )
    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    Flux<TaskResponse> getAllTasks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return taskService.getAllTasks(page, size);
    }

    @Operation(
            summary = "Returns task by ID",
            description = "Returns reactive Mono object of TaskResponse",
            parameters = {
                    @Parameter(name = "id", description = "ID of Task to be found.")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Task successfully found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Task with given ID was not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    Mono<TaskResponse> getTaskById(@PathVariable("id") String id) {
        return taskService.getTaskById(id);
    }

    @Operation(
            summary = "Creates new Task",
            description = "Creates new Task depending on TaskRequest request body and returns reactive Mono object of TaskResponse",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for new Task",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task successfully created."),
                    @ApiResponse(
                            responseCode = "400", description = "Incorrect Task data provided.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "User with given ID was not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(CREATED)
    Mono<TaskResponse> saveTask(@Valid @RequestBody Mono<TaskRequest> request) {
        return request.flatMap(taskService::saveTask);
    }

    @Operation(
            summary = "Updates existing Task",
            description = "Updates existing Task depending on TaskRequest request body.",
            parameters = {
                    @Parameter(name = "id", description = "ID of existing Task")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New data for existing Task",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task successfully updated."),
                    @ApiResponse(
                            responseCode = "404", description = "Task/User with given ID was not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> updateTask(
            @PathVariable("id") String id,
            @Valid @RequestBody TaskRequest dto) {
        return taskService.updateTask(id, dto);
    }

    @Operation(
            summary = "Deletes Task by ID",
            description = "Deletes Task by ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of Task to be deleted.")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task successfully deleted.")
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> deleteTaskById(@PathVariable("id") String id) {
        return taskService.deleteTaskById(id);
    }

    @Operation(
            summary = "Returns all Tasks associated with User.",
            description = "Returns reactive Flux stream of TaskResponse associated with User.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of User whose Tasks to be found.")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Tasks successfully found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
                    )
            }
    )
    @GetMapping("/user/{userId}")
    Flux<TaskResponse> getAllUserTasks(@PathVariable("userId") String userId) {
        return taskService.getAllUserTasks(userId);
    }
}
