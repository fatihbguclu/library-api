package com.ft.library.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.library.exception.BookNotFoundException;
import com.ft.library.model.dto.request.CreateBookRequest;
import com.ft.library.model.entity.Book;
import com.ft.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Book cleanCode;

    private Book effectiveJava;

    private CreateBookRequest createBookRequest;

    @BeforeEach
    void setup() {
        cleanCode = Book.builder()
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(10)
                .category("Programming")
                .build();

        effectiveJava = Book.builder()
                .title("Effective Java")
                .isbn("9780134685991")
                .author("Joshua Bloch")
                .publishYear(2018)
                .quantityAvailable(5)
                .category("Programming")
                .build();

        createBookRequest = new CreateBookRequest("Clean Code", "9780132350884",
                "Robert C. Martin", 2008, 10, "Programming");
    }

    @Test
    void getAllBook_shouldReturnAllBook() throws Exception {
        when(bookService.getAllBook()).thenReturn(List.of(cleanCode, effectiveJava));

        mockMvc.perform(get("/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.size()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.data[1].title").value("Effective Java"));
    }

    @Test
    void getBookById_shouldReturnBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(cleanCode);

        mockMvc.perform(get("/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.author").value("Robert C. Martin"));
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldReturnErrorMessage() throws Exception {
        when(bookService.getBookById(999L))
                .thenThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(get("/v1/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Book not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void getBookByIsbn_shouldReturnBookByIsbn() throws Exception {
        when(bookService.getBookByIsbn("9780132350884")).thenReturn(cleanCode);

        mockMvc.perform(get("/v1/books/isbn/9780132350884"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.author").value("Robert C. Martin"));
    }

    @Test
    void getBookByIsbn_WhenBookNotFound_ShouldReturnErrorMessage() throws Exception {
        when(bookService.getBookByIsbn("fake-isbn")).thenThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(get("/v1/books/isbn/fake-isbn"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Book not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void createBook_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cleanCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void createBook_WhenServiceThrowException_ShouldReturnErrorMessage() throws Exception {
        doThrow(new RuntimeException()).when(bookService).createBook(any(CreateBookRequest.class));

        mockMvc.perform(post("/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Something Went Wrong"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void updateBook_shouldReturnSuccess() throws Exception {
        mockMvc.perform(put("/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cleanCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldReturnErrorMessage() throws Exception {
        doThrow(new BookNotFoundException("Book not found")).when(bookService).updateBook(any(Long.class), any(CreateBookRequest.class));

        mockMvc.perform(put("/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Book not found"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}