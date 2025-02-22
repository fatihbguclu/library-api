package com.ft.library.service.impl;

import com.ft.library.model.entity.Book;
import com.ft.library.repository.BookRepository;
import com.ft.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(long l) {
        return bookRepository.findById(l).orElse(new Book());
    }
}
