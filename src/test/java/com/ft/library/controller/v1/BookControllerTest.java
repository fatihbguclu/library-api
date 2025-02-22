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
                                Book.builder().title("asd").author("asd").build(),
                                Book.builder().title("asd1").author("asd1").build()
                        )
                );

        mockMvc.perform(get("/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("asd"));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        when(bookService.getBookById(1L))
                .thenReturn(
                        Book.builder().title("asd").author("asd").build()
                );

        mockMvc.perform(get("/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("asd"))
                .andExpect(jsonPath("$.author").value("asd"));
    }
}