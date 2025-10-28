package com.reactive_task_management.to_do_list.task;

import com.reactive_task_management.to_do_list.TestConfig;
import com.reactive_task_management.to_do_list.exception.CustomExceptionHandler;
import com.reactive_task_management.to_do_list.exception.ErrorResponse;
import com.reactive_task_management.to_do_list.exception.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = TaskController.class)
@Import({TestConfig.class, CustomExceptionHandler.class})
class TaskControllerTest {

    /** WebTestClient because it is "not-blocking" way to access endpoints*/
    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    TaskService taskService;

    @Test
    void getById_correctId_correctResponseAndOkStatus() {
        // given
        final var dummyId = "dummyId";
        final var dummyTaskResponse = new TaskResponse(dummyId, "dummyTitle", "dummyDescription", TaskStatus.CREATED, LocalDateTime.now(), "dummyUserId");
        when(taskService.getTaskById(dummyId)).thenReturn(Mono.just(dummyTaskResponse));

        // when & then
        webTestClient.get()
                .uri("/tasks/{id}", dummyId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(dummyId)
                .jsonPath("$.title").isEqualTo("dummyTitle");
    }

    @Test
    void getById_notExistingId_notFoundStatus() {
        // given
        final var dummyId = "dummyId";
        when(taskService.getTaskById(dummyId)).thenReturn(Mono.error(new TaskNotFoundException(dummyId)));

        // when & then
        webTestClient.get()
                .uri("/tasks/{id}", dummyId)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .consumeWith(response -> {
                    final var body = response.getResponseBody();
                    assertTrue(body.message().contains(dummyId));
                });
    }

    @Test
    void saveTask_correctTask_correctTaskResponseAndCreatedStatus() {

    }

    @Test
    void saveTask_incorrectUserId_notFoundStatus() {

    }


}
