package com.ft.library.exception;

import com.ft.library.model.dto.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleBookNotFoundException(BookNotFoundException e) {
        GenericResponse<?> notFoundResponse = GenericResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
    }

}
