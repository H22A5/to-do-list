package com.reactive_task_management.to_do_list.user;

import com.reactive_task_management.to_do_list.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User related operations")
class UserController {

    private final UserService userService;

    @Operation(
            summary = "Creates User",
            description = "Creates new User depending on UserRequest request body",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for new User",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully created."),
                    @ApiResponse(
                            responseCode = "400", description = "Incorrect User data provided.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(CREATED)
    Mono<UserResponse> saveUser(@Valid @RequestBody Mono<UserRequest> request) {
        return request.flatMap(userService::saveUser);
    }

}
