package com.reactive_task_management.to_do_list.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;

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
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(RuntimeException ex, ServerWebExchange exchange) {
        return new ErrorResponse(
                Instant.now().toString(),
                BAD_REQUEST.value(),
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );
    }
}
