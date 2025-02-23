package com.ft.library.controller.v1;

import com.ft.library.model.entity.Book;
import com.ft.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void shouldReturnAllBook() throws Exception {
        when(bookService.getAllBook())
                .thenReturn(
                        List.of(
                                Book.builder().title("Clean Code").author("Robert C. Martin").build(),
                                Book.builder().title("Effective Java").author("Joshua Bloch").build()
                        )
                );

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
    void shouldReturnBookById() throws Exception {
        when(bookService.getBookById(1L))
                .thenReturn(
                        Book.builder().title("Clean Code").author("Robert C. Martin").build()
                );

        mockMvc.perform(get("/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.author").value("Robert C. Martin"));
    }
}