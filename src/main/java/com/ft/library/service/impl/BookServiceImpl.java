package com.ft.library.service.impl;

import com.ft.library.exception.BookNotFoundException;
import com.ft.library.model.dto.request.CreateBookRequest;
import com.ft.library.model.entity.Book;
import com.ft.library.repository.BookRepository;
import com.ft.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Book getBookById(long id) {
        return bookRepository.findBookById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findBookByIsbn(isbn).orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    @Override
    @Transactional
    public void createBook(CreateBookRequest bookRequest) {
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .isbn(bookRequest.getIsbn())
                .author(bookRequest.getAuthor())
                .publishYear(bookRequest.getPublishYear())
                .quantityAvailable(bookRequest.getQuantityAvailable())
                .category(bookRequest.getCategory()).build();

        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void updateBook(long id, CreateBookRequest bookRequest) {
        Book book = bookRepository.findBookById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setAuthor(bookRequest.getAuthor());
        book.setPublishYear(bookRequest.getPublishYear());
        book.setQuantityAvailable(bookRequest.getQuantityAvailable());
        book.setCategory(bookRequest.getCategory());

        bookRepository.save(book);
    }
}
