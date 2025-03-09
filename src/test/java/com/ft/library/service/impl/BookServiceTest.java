package com.ft.library.service.impl;

import com.ft.library.model.dto.request.CreateBookRequest;
import com.ft.library.model.entity.Book;
import com.ft.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void createBook_shouldCreateBook() {
        CreateBookRequest createBookRequest = new CreateBookRequest("Clean Code", "9780132350884",
                "Robert C. Martin", 2008, 10, "Programming");

        Book savedBook = Book.builder()
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(10)
                .category("Programming")
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        bookService.createBook(createBookRequest);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_whenRepositoryThrowException_shouldThrowException() {
        CreateBookRequest createBookRequest = new CreateBookRequest("Clean Code", "9780132350884",
                "Robert C. Martin", 2008, 10, "Programming");

        when(bookRepository.save(any(Book.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> bookService.createBook(createBookRequest));
    }
}