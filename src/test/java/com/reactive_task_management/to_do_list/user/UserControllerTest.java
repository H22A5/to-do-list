package com.reactive_task_management.to_do_list.user;

import com.reactive_task_management.to_do_list.TestConfig;
import com.reactive_task_management.to_do_list.exception.CustomExceptionHandler;
import com.reactive_task_management.to_do_list.exception.ErrorResponse;
import com.reactive_task_management.to_do_list.exception.user.UserAlreadyExists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Needed to {@code @Import}
 *      <p>{@link TestConfig} with MongoMappingContext bean to prevent application from creating persistence layer</p>
 *      <p>{@link CustomExceptionHandler} to have full control of Http statuses returned from TaskController</p>
 */
@WebFluxTest(controllers = UserController.class)
@Import({TestConfig.class, CustomExceptionHandler.class})
public class UserControllerTest {

    /** {@link WebTestClient} because it is "not-blocking" way to access endpoints*/
    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    UserService userService;

    @Test
    void saveUser_correctUserRequest_correctUserResponseAndCreatedStatus() {
        // given
        final var dummyUserRequest = new UserRequest("dummyUsername");
        final var dummyUserResponse = new UserResponse("dummyId", "dummyUsername");

        when(userService.saveUser(dummyUserRequest)).thenReturn(Mono.just(dummyUserResponse));

        // when & then
        webTestClient.post()
                .uri("/users")
                .bodyValue(dummyUserRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(dummyUserResponse);
    }

    @Test
    void saveUser_incorrectUsername_badRequestStatus() {
        // given
        final var dummyUserRequest = new UserRequest(null);

        // when & then
        webTestClient.post()
                .uri("/users")
                .bodyValue(dummyUserRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("Username cannot be blank.") && messages.size() == 1);
                });
    }

    @Test
    void saveUser_existingUsername_badRequestStatus() {
        // given
        final var existingUsername = "existing";
        final var dummyUserRequest = new UserRequest(existingUsername);

        when(userService.saveUser(dummyUserRequest)).thenReturn(Mono.error(new UserAlreadyExists(existingUsername)));

        // when & then
        webTestClient.post()
                .uri("/users")
                .bodyValue(dummyUserRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    final var messages = Optional.ofNullable(result.getResponseBody()).orElse(ErrorResponse.empty()).messages();
                    assertTrue(messages.contains("User with [username=%s] already exists.".formatted(existingUsername)) && messages.size() == 1);
                });
    }
}
