package com.demo.producer.exception;

import com.demo.producer.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = InvalidException.class)
    public Response invalidException(InvalidException e) {
        Response response = new Response();
        response.setStatusCode(StatusCode.FAILED.getCode());
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Response response = new Response();
        response.setStatusCode(StatusCode.FAILED.getCode());
        response.setMessage(e.getFieldError().getDefaultMessage());
        return response;
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Response runtimeException(RuntimeException e) {
        Response response = new Response();
        response.setStatusCode(StatusCode.FAILED.getCode());
        response.setMessage(e.getMessage());
        return response;
    }
}
