package com.ft.library.service.impl;

import com.ft.library.exception.BookNotFoundException;
import com.ft.library.model.dto.request.CreateBookRequest;
import com.ft.library.model.entity.Book;
import com.ft.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;


    private List<Book> savedBooks;

    private Book existingBook;

    private CreateBookRequest updateBookRequest;

    private CreateBookRequest createBookRequest;

    @BeforeEach
    void setup() {
        savedBooks = List.of(
                Book.builder()
                        .id(1L)
                        .title("Clean Code")
                        .isbn("9780132350884")
                        .author("Robert C. Martin")
                        .publishYear(2008)
                        .quantityAvailable(10)
                        .category("Programming")
                        .build(),
                Book.builder()
                        .id(2L)
                        .title("Effective Java")
                        .isbn("9780134685991")
                        .author("Joshua Bloch")
                        .publishYear(2018)
                        .quantityAvailable(5)
                        .category("Programming")
                        .build()
        );

        existingBook = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(10)
                .category("Programming")
                .build();

        updateBookRequest = new CreateBookRequest("Effective Java", "9780134685991",
                "Joshua Bloch", 2018, 5, "Programming");

        createBookRequest = new CreateBookRequest("Clean Code", "9780132350884",
                "Robert C. Martin", 2008, 10, "Programming");
    }

    @Test
    void createBook_shouldCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        bookService.createBook(createBookRequest);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_whenRepositoryThrowException_shouldThrowException() {
        when(bookRepository.save(any(Book.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> bookService.createBook(createBookRequest));
    }

    @Test
    void getAllBook_shouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(savedBooks);

        List<Book> allBook = bookService.getAllBook();

        verify(bookRepository, times(1)).findAll();
        assertEquals(2, allBook.size());
        assertEquals("Clean Code", allBook.get(0).getTitle());
        assertEquals("Effective Java", allBook.get(1).getTitle());
    }

    @Test
    void getAllBook_whenEmpty_shouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());

        List<Book> allBook = bookService.getAllBook();

        verify(bookRepository, times(1)).findAll();
        assertNotNull(allBook);
        assertTrue(allBook.isEmpty());
    }

    @Test
    void getBookById_shouldReturnBook() {
        when(bookRepository.findBookById(1L)).thenReturn(Optional.of(savedBooks.get(0)));

        Book book = bookService.getBookById(1L);

        verify(bookRepository, times(1)).findBookById(1L);
        assertNotNull(book);
        assertEquals(1L, book.getId());
        assertEquals("Clean Code", book.getTitle());
    }

    @Test
    void getBookById_whenBookNotFound_shouldThrowException() {
        when(bookRepository.findBookById(999L)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.getBookById(999L));

        verify(bookRepository, times(1)).findBookById(999L);
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void getBookByIsbn_shouldReturnBook() {
        when(bookRepository.findBookByIsbn("9780132350884")).thenReturn(Optional.of(savedBooks.get(0)));

        Book book = bookService.getBookByIsbn("9780132350884");

        verify(bookRepository, times(1)).findBookByIsbn("9780132350884");
        assertNotNull(book);
        assertEquals("9780132350884", book.getIsbn());
        assertEquals("Clean Code", book.getTitle());
    }

    @Test
    void getBookByIsbn_whenBookNotFound_shouldThrowException() {
        when(bookRepository.findBookByIsbn("fake-isbn")).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.getBookByIsbn("fake-isbn"));

        verify(bookRepository, times(1)).findBookByIsbn("fake-isbn");
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void updateBook_shouldUpdateBook() {
        when(bookRepository.findBookById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        bookService.updateBook(1L, updateBookRequest);

        verify(bookRepository, times(1)).findBookById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));

        assertEquals(existingBook.getTitle(), updateBookRequest.getTitle());
        assertEquals(existingBook.getIsbn(), updateBookRequest.getIsbn());
        assertEquals(existingBook.getAuthor(), updateBookRequest.getAuthor());
        assertEquals(existingBook.getPublishYear(), updateBookRequest.getPublishYear());
        assertEquals(existingBook.getQuantityAvailable(), updateBookRequest.getQuantityAvailable());
        assertEquals(existingBook.getCategory(), updateBookRequest.getCategory());
    }

    @Test
    void updateBook_whenBookNotFound_shouldThrowException() {
        when(bookRepository.findBookById(999L)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.updateBook(999L, updateBookRequest));

        verify(bookRepository, times(1)).findBookById(999L);
        assertEquals("Book not found", exception.getMessage());
    }
}