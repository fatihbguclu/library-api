package com.ft.library.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBookRequest {

    private String title;
    private String isbn;
    private String author;
    private Integer publishYear;
    private Integer quantityAvailable;
    private String category;

}
