package com.reactive_task_management.to_do_list.exception;

import com.reactive_task_management.to_do_list.exception.task.TaskNotFoundException;
import com.reactive_task_management.to_do_list.exception.user.UserAlreadyExists;
import com.reactive_task_management.to_do_list.exception.user.UserNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({TaskNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(RuntimeException ex, ServerWebExchange exchange) {
        return new ErrorResponse(
                Instant.now().toString(),
                NOT_FOUND.value(),
                List.of(ex.getMessage()),
                exchange.getRequest().getPath().value()
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(WebExchangeBindException ex, ServerWebExchange exchange) {
        return new ErrorResponse(
                Instant.now().toString(),
                BAD_REQUEST.value(),
                ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList(),
                exchange.getRequest().getPath().value()
        );
    }

    @ExceptionHandler(UserAlreadyExists.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExists ex, ServerWebExchange exchange) {
        return new ErrorResponse(
                Instant.now().toString(),
                BAD_REQUEST.value(),
                List.of(ex.getMessage()),
                exchange.getRequest().getPath().value()
        );
    }
}
