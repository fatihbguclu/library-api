package com.ft.library.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.library.exception.BookNotAvailableException;
import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;
import com.ft.library.model.enums.BorrowStatus;
import com.ft.library.service.BorrowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowController.class)
public class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BorrowService borrowService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createBorrowRequest_whenSuccess_thenReturnCreateBorrowResponse() throws Exception {
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        CreateBorrowResponse response = new CreateBorrowResponse(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                BorrowStatus.ACTIVE
        );

        when(borrowService.borrowBook(any(CreateBorrowRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.borrowDate").exists())
                .andExpect(jsonPath("$.data.dueDate").exists())
                .andExpect(jsonPath("$.data.borrowStatus").value("ACTIVE"));
    }

    @Test
    void createBorrowRequest_whenBookAlreadyBorrowed_thenReturnErrorResponse() throws Exception {
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        when(borrowService.borrowBook(any(CreateBorrowRequest.class)))
                .thenThrow(new BookNotAvailableException("Book Already Borrowed"));

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Book Already Borrowed"));
    }

    @Test
    void createBorrowRequest_whenBookStockNotAvailable_thenReturnErrorResponse() throws Exception {
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        when(borrowService.borrowBook(any(CreateBorrowRequest.class)))
                .thenThrow(new BookNotAvailableException("Book Stock Not Available"));

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Book Stock Not Available"));
    }

    @Test
    void createBorrowRequest_whenMemberStatusSuspended_thenReturnErrorResponse() throws Exception {
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        when(borrowService.borrowBook(any(CreateBorrowRequest.class)))
                .thenThrow(new BookNotAvailableException("Member Status is Suspended"));

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Member Status is Suspended"));
    }

}
