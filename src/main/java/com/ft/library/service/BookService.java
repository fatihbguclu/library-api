package com.ft.library.service;

import com.ft.library.model.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {
    List<Book> getAllBook();
}
