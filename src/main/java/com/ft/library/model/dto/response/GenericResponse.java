package com.ft.library.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenericResponse<T> {

    private String status;
    private String message;
    private T data;

    public static <T> GenericResponse<T> of(String status, String message, T data) {
        return GenericResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
