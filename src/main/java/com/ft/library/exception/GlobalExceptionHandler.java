package com.ft.library.exception;

import com.ft.library.model.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleBookNotFoundException(BookNotFoundException e) {
        ApiResponse<?> notFoundResponse = ApiResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleMemberNotFoundException(MemberNotFoundException e) {
        ApiResponse<?> notFoundResponse = ApiResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleMemberAlreadyExistsException(MemberAlreadyExistsException e) {
        ApiResponse<?> existsResponse = ApiResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(existsResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ApiResponse<?>> handleBookNotAvailableException(BookNotAvailableException e) {
        ApiResponse<?> existsResponse = ApiResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(existsResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BorrowRecordNotFound.class)
    public ResponseEntity<ApiResponse<?>> handleBorrowRecordNotFound(BorrowRecordNotFound e) {
        ApiResponse<?> existsResponse = ApiResponse.of("Error", e.getMessage(), null);
        return new ResponseEntity<>(existsResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleUndefinedException(RuntimeException e) {
        ApiResponse<?> exceptionResponse = ApiResponse.of("Error", "Something Went Wrong", null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
