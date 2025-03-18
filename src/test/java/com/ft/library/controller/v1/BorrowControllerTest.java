package com.ft.library.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.library.exception.BookNotAvailableException;
import com.ft.library.exception.BorrowRecordNotFound;
import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.entity.Book;
import com.ft.library.model.entity.BorrowEntry;
import com.ft.library.model.entity.Member;
import com.ft.library.model.enums.BorrowStatus;
import com.ft.library.model.enums.MembershipStatus;
import com.ft.library.service.BorrowService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private static Book book;

    private static Member member;

    private BorrowEntry borrowEntry;

    @BeforeAll
    static void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .quantityAvailable(10).build();
        member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipDate(LocalDateTime.now())
                .membershipStatus(MembershipStatus.ACTIVE).build();
    }

    @BeforeEach
    void setUpEach() {
        borrowEntry = BorrowEntry.builder()
                .id(1L)
                .member(member)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(7))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE).build();
    }

    @Test
    void createBorrowRequest_whenSuccess_thenReturnCreateBorrowResponse() throws Exception {
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);

        when(borrowService.borrowBook(any(CreateBorrowRequest.class))).thenReturn(borrowEntry);

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

    @Test
    void returnBook_whenSuccess_thenReturnReturnBorrowResponse() throws Exception {
        long borrowId = 1L;
        borrowEntry.setReturnDate(LocalDateTime.now().plusDays(3));
        borrowEntry.setBorrowStatus(BorrowStatus.RETURNED);

        when(borrowService.returnBook(borrowId)).thenReturn(borrowEntry);

        mockMvc.perform(put("/v1/borrow/return/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.borrowDate").exists())
                .andExpect(jsonPath("$.data.returnDate").exists())
                .andExpect(jsonPath("$.data.dueDate").exists())
                .andExpect(jsonPath("$.data.borrowStatus").value("RETURNED"))
                .andExpect(jsonPath("$.data.penaltyAmount").value(0));
    }

    @Test
    void returnBook_whenBorrowRecordNotFound_thenReturnErrorResponse() throws Exception {
        long borrowId = 1L;

        when(borrowService.returnBook(borrowId)).thenThrow(new BorrowRecordNotFound("Borrow Record Not Found"));

        mockMvc.perform(put("/v1/borrow/return/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Borrow Record Not Found"));
    }

    @Test
    void returnBook_whenDueDatePassed_thenReturnBorrowResponseWithPenalty() throws Exception {
        long borrowId = 1L;
        borrowEntry.setReturnDate(LocalDateTime.now().plusDays(10));
        borrowEntry.setBorrowStatus(BorrowStatus.OVERDUE);
        borrowEntry.setPenaltyAmount(BigDecimal.valueOf(3));

        when(borrowService.returnBook(borrowId)).thenReturn(borrowEntry);

        mockMvc.perform(put("/v1/borrow/return/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.borrowDate").exists())
                .andExpect(jsonPath("$.data.returnDate").exists())
                .andExpect(jsonPath("$.data.dueDate").exists())
                .andExpect(jsonPath("$.data.borrowStatus").value("OVERDUE"))
                .andExpect(jsonPath("$.data.penaltyAmount").value(3));
    }
}
