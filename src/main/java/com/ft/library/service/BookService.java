package com.ft.library.service;

import com.ft.library.model.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBook();

    Book getBookById(long l);
}
