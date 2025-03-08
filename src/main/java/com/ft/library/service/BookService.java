package com.ft.library.service;

import com.ft.library.model.dto.request.CreateBookRequest;
import com.ft.library.model.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBook();

    Book getBookById(long l);

    void createBook(CreateBookRequest bookRequest);

    void updateBook(long id, CreateBookRequest bookRequest);
}
