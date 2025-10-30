package com.reactive_task_management.to_do_list.task;

import com.reactive_task_management.to_do_list.TestConfig;
import com.reactive_task_management.to_do_list.exception.CustomExceptionHandler;
import com.reactive_task_management.to_do_list.exception.ErrorResponse;
import com.reactive_task_management.to_do_list.exception.task.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.reactive_task_management.to_do_list.task.TaskStatus.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Needed to {@code @Import}
 *      <p>{@link TestConfig} with MongoMappingContext bean to prevent application from creating persistence layer</p>
 *      <p>{@link CustomExceptionHandler} to have full control of Http statuses returned from TaskController</p>
 */
@WebFluxTest(controllers = TaskController.class)
@Import({TestConfig.class, CustomExceptionHandler.class})
class TaskControllerTest {

    /** {@link WebTestClient} because it is "not-blocking" way to access endpoints*/
    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    TaskService taskService;

    @Test
    void getTaskById_correctId_correctResponseAndOkStatus() {
        // given
        final var dummyId = "dummyId";
        final var dummyTaskResponse = new TaskResponse(dummyId, "dummyTitle", "dummyDescription", CREATED, LocalDateTime.now(), "dummyUserId");
        when(taskService.getTaskById(dummyId)).thenReturn(Mono.just(dummyTaskResponse));

        // when & then
        webTestClient.get()
                .uri("/tasks/{id}", dummyId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(dummyTaskResponse);
    }

    @Test
    void getTaskById_incorrectId_notFoundStatus() {
        // given
        final var incorrectId = "incorrectId";
        when(taskService.getTaskById(incorrectId)).thenReturn(Mono.error(new TaskNotFoundException(incorrectId)));

        // when & then
        webTestClient.get()
                .uri("/tasks/{id}", incorrectId)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("Task[id=%s] not found.".formatted(incorrectId)) && messages.size() == 1);
                });
    }

    @Test
    void saveTask_correctTaskRequest_correctTaskResponseAndCreatedStatus() {
        // given
        final var dummyTaskRequest = new TaskRequest("dummyTitle", "dummyDescription", CREATED, "dummyUserId");
        final var dummyTaskResponse = new TaskResponse("dummyId", "dummyTitle", "dummyDescription", CREATED, LocalDateTime.now(), "dummyUserId");
        when(taskService.saveTask(dummyTaskRequest)).thenReturn(Mono.just(dummyTaskResponse));

        // when & then
        webTestClient.post()
                .uri("/tasks")
                .bodyValue(dummyTaskRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(TaskResponse.class)
                .consumeWith(result -> {
                    final var body = result.getResponseBody();
                    assertEquals(dummyTaskResponse, body);
                });
    }

    @Test
    void saveTask_incorrectTaskTitle_notFoundStatus() {
        // given
        final var dummyTaskRequest = new TaskRequest(null, "dummyDescription", CREATED, "dummyUserId");

        // when & then
        webTestClient.post()
                .uri("/tasks")
                .bodyValue(dummyTaskRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("Title cannot be blank.") && messages.size() == 1);
                });
    }

    @Test
    void saveTask_incorrectUserId_notFoundStatus() {
        // given
        final var dummyTaskRequest = new TaskRequest("dummyTitle", "dummyDescription", CREATED, null);

        // when & then
        webTestClient.post()
                .uri("/tasks")
                .bodyValue(dummyTaskRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("User id cannot be blank.") && messages.size() == 1);
                });
    }

    @Test
    void getAllTasks_correctSizeAndPage_listOfTasksAndOkStatus() {
        // given
        final var size = 30;
        final var page = 1;

        final var dummyResponse1 = new TaskResponse("dummyId", "dummyTitle", "dummyDescription", CREATED, LocalDateTime.now(), "dummyUserId");
        final var dummyResponse2 = new TaskResponse("dummyId2", "dummyTitle2", "dummyDescription2", CREATED, LocalDateTime.now(), "dummyUserId2");

        when(taskService.getAllTasks(page, size)).thenReturn(Flux.just(dummyResponse1, dummyResponse2));

        // when & then
        webTestClient.get()
                .uri("/tasks?size=" + size + "&page=" + page)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(2)
                .contains(dummyResponse1, dummyResponse2);
    }

    @Test
    void getAllTasks_noSizeAndPage_listOfTasksAndOkStatus() {
        // given
        final var dummyResponse1 = new TaskResponse("dummyId", "dummyTitle", "dummyDescription", CREATED, LocalDateTime.now(), "dummyUserId");
        final var dummyResponse2 = new TaskResponse("dummyId2", "dummyTitle2", "dummyDescription2", CREATED, LocalDateTime.now(), "dummyUserId2");

        when(taskService.getAllTasks(0, 10)).thenReturn(Flux.just(dummyResponse1, dummyResponse2));

        // when & then
        webTestClient.get()
                .uri("/tasks")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(2)
                .contains(dummyResponse1, dummyResponse2);
    }

    @Test
    void updateTask_correctIdAndTaskRequest_noContentStatus() {
        // given
        final var dummyId = "dummyId";
        final var dummyTaskRequest = new TaskRequest("dummyTitle", "dummyDescription", CREATED, "dummyUserId");

        when(taskService.updateTask(dummyId, dummyTaskRequest)).thenReturn(Mono.empty());

        // when & then
        webTestClient.put()
                .uri("/tasks/{id}", dummyId)
                .bodyValue(dummyTaskRequest)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody()
                .isEmpty();
    }

    @Test
    void updateTask_incorrectId_notFoundStatus() {
        // given
        final var incorrectId = "incorrectId";
        final var dummyTaskRequest = new TaskRequest("dummyTitle", "dummyDescription", CREATED, "dummyUserId");

        when(taskService.updateTask(incorrectId, dummyTaskRequest)).thenReturn(Mono.error(new TaskNotFoundException(incorrectId)));

        // when & then
        webTestClient.put()
                .uri("/tasks/{id}", incorrectId)
                .bodyValue(dummyTaskRequest)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("Task[id=%s] not found.".formatted(incorrectId)) && messages.size() == 1);
                });
    }

    @Test
    void updateTask_incorrectTitle_badRequestStatus() {
        // given
        final var dummyId = "dummyId";
        final var dummyTaskRequest = new TaskRequest(null, "dummyDescription", CREATED, "dummyUserId");

        // when & then
        webTestClient.put()
                .uri("/tasks/{id}", dummyId)
                .bodyValue(dummyTaskRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("Title cannot be blank.") && messages.size() == 1);
                });
    }

    @Test
    void updateTask_incorrectUserId_badRequestStatus() {
        // given
        final var dummyId = "dummyId";
        final var dummyTaskRequest = new TaskRequest("dummyTitle", "dummyDescription", CREATED, null);

        // when & then
        webTestClient.put()
                .uri("/tasks/{id}", dummyId)
                .bodyValue(dummyTaskRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("User id cannot be blank.") && messages.size() == 1);
                });
    }

    @Test
    void deleteTaskById_correctId_noContentStatus() {
        // given
        final var dummyId = "dummyId";

        when(taskService.deleteTaskById(dummyId)).thenReturn(Mono.empty());

        // when & then
        webTestClient.delete()
                .uri("/tasks/{id}", dummyId)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody()
                .isEmpty();
    }

    @Test
    void getAllUserTasks_correctUserId_listOfSpecificUserTasksAndOkStatus() {
        // given
        final var dummyUserId = "dummyUserId";

        final var dummyResponse1 = new TaskResponse("dummyId", "dummyTitle", "dummyDescription", CREATED, LocalDateTime.now(), "dummyUserId");
        final var dummyResponse2 = new TaskResponse("dummyId2", "dummyTitle2", "dummyDescription2", CREATED, LocalDateTime.now(), "dummyUserId2");

        when(taskService.getAllUserTasks(dummyUserId)).thenReturn(Flux.just(dummyResponse1, dummyResponse2));

        // when & then
        webTestClient.get()
                .uri("/tasks/user/{userId}", dummyUserId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(2)
                .contains(dummyResponse1, dummyResponse2);
    }
}
