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

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleMemberNotFoundException(MemberNotFoundException e) {
        GenericResponse<?> notFoundResponse = GenericResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ResponseEntity<GenericResponse<?>> handleMemberAlreadyExistsException(MemberAlreadyExistsException e) {
        GenericResponse<?> existsResponse = GenericResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(existsResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<GenericResponse<?>> handleBookNotAvailableException(BookNotAvailableException e) {
        GenericResponse<?> existsResponse = GenericResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(existsResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericResponse<?>> handleUndefinedException(RuntimeException e) {
        GenericResponse<?> exceptionResponse = GenericResponse.of("Error", "Something Went Wrong", null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
