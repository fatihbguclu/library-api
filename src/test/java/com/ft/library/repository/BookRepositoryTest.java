package com.ft.library.repository;

import com.ft.library.model.entity.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setup() {
        Book cleanCode = Book.builder()
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(10)
                .category("Programming")
                .build();

        Book effectiveJava = Book.builder()
                .title("Effective Java")
                .isbn("9780134685991")
                .author("Joshua Bloch")
                .publishYear(2018)
                .quantityAvailable(5)
                .category("Programming")
                .build();

        testEntityManager.persistAndFlush(cleanCode);
        testEntityManager.persist(effectiveJava);
    }

    @AfterEach
    void tearDown() {
        testEntityManager.clear();
    }

    @Test
    @Order(1)
    void getBookById_shouldReturnBook() {
        Book book = bookRepository.findBookById(1L).orElse(null);

        assertNotNull(book);
        assertEquals("Clean Code", book.getTitle());
        assertEquals("9780132350884", book.getIsbn());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals(2008, book.getPublishYear());
        assertEquals(10, book.getQuantityAvailable());
        assertEquals("Programming", book.getCategory());

        Book book2 = bookRepository.findBookById(2L).orElse(null);

        assertNotNull(book2);
        assertEquals("Effective Java", book2.getTitle());
        assertEquals("9780134685991", book2.getIsbn());
        assertEquals("Joshua Bloch", book2.getAuthor());
        assertEquals(2018, book2.getPublishYear());
        assertEquals(5, book2.getQuantityAvailable());
        assertEquals("Programming", book2.getCategory());
    }

    @Test
    @Order(2)
    void getAllBooks_shouldReturnAllBooks() {
        List<Book> bookList = bookRepository.findAll();

        assertThat(bookList).isNotEmpty();
        assertThat(bookList).hasSize(2);
        assertThat(bookList).extracting(Book::getTitle).contains("Clean Code", "Effective Java");
    }

    @Test
    @Order(3)
    void getByIsbn_shouldReturnBook() {
        Book book = bookRepository.findBookByIsbn("9780132350884").orElse(null);

        assertNotNull(book);
        assertEquals("Clean Code", book.getTitle());
        assertEquals("9780132350884", book.getIsbn());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals(2008, book.getPublishYear());
        assertEquals(10, book.getQuantityAvailable());
        assertEquals("Programming", book.getCategory());
    }

}