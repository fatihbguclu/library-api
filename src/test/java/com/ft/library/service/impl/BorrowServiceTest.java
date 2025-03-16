package com.ft.library.service.impl;

import com.ft.library.exception.BookNotAvailableException;
import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;
import com.ft.library.model.dto.response.ReturnBorrowResponse;
import com.ft.library.model.entity.Book;
import com.ft.library.model.entity.BorrowRecord;
import com.ft.library.model.entity.Member;
import com.ft.library.model.enums.BorrowStatus;
import com.ft.library.model.enums.MembershipStatus;
import com.ft.library.repository.BorrowRepository;
import com.ft.library.service.BookService;
import com.ft.library.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookService bookService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @Test
    void borrowBook_whenValid_thenReturnResponse() {
        LocalDateTime borrowDate = LocalDateTime.now();
        // Arrange
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(10)
                .category("Programming")
                .build();
        Member member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .build();

        when(bookService.getBookById(1L)).thenReturn(book);
        when(memberService.getMemberById(1L)).thenReturn(member);

        // Act
        CreateBorrowResponse response = borrowService.borrowBook(request);

        // Assert
        assertEquals(borrowDate, response.getBorrowDate());
        assertEquals(borrowDate.plusDays(7), response.getDueDate());
        assertEquals(BorrowStatus.ACTIVE, response.getBorrowStatus());
        verify(bookService, times(1)).getBookById(1L);
        verify(memberService, times(1)).getMemberById(1L);
    }

    @Test
    void borrowBook_whenBookAlreadyBorrowed_thenThrowException() {
        // Arrange
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        when(borrowRepository.existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(request.getBookId(), request.getMemberId(), BorrowStatus.ACTIVE)).thenReturn(true);

        // Act & Assert
        BookNotAvailableException exception = assertThrows(BookNotAvailableException.class, () -> borrowService.borrowBook(request));
        assertEquals("Book Already Borrowed", exception.getMessage());
        verify(bookService, never()).getBookById(anyLong());
        verify(memberService, never()).getMemberById(anyLong());
        verify(borrowRepository, never()).save(any());
    }

    @Test
    void borrowBook_whenBookStockNotAvailable_thenThrowException() {
        // Arrange
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(0)
                .category("Programming")
                .build();
        when(bookService.getBookById(1L)).thenReturn(book);

        // Act & Assert
        BookNotAvailableException exception = assertThrows(BookNotAvailableException.class, () -> borrowService.borrowBook(request));
        assertEquals("Book Stock Not Available", exception.getMessage());
        verify(memberService, never()).getMemberById(anyLong());
        verify(borrowRepository, never()).save(any());
    }

    @Test
    void borrowBook_whenMemberStatusSuspended_thenThrowException() {
        // Arrange
        CreateBorrowRequest request = new CreateBorrowRequest(1L, 1L);
        Member member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.SUSPENDED)
                .build();
        when(memberService.getMemberById(1L)).thenReturn(member);

        // Act & Assert
        BookNotAvailableException exception = assertThrows(BookNotAvailableException.class, () -> borrowService.borrowBook(request));
        assertEquals("Member Status is Suspended", exception.getMessage());
        verify(bookService, never()).getBookById(anyLong());
        verify(borrowRepository, never()).save(any());
    }

    @Test
    void returnBook_whenValid_thenReturnResponse() {
        // Arrange
        LocalDateTime returnDate = LocalDateTime.now();
        long borrowRecordId = 1L;
        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(0)
                .category("Programming")
                .build();
        Member member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE)
                .build();
        BorrowRecord borrowRecord = BorrowRecord.builder()
                .id(borrowRecordId)
                .book(book)
                .member(member)
                .borrowDate(returnDate.minusDays(7))
                .dueDate(returnDate.plusDays(1))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE)
                .build();

        when(borrowRepository.findById(borrowRecordId)).thenReturn(Optional.of(borrowRecord));

        // Act
        ReturnBorrowResponse response = borrowService.returnBook(borrowRecordId);

        // Assert
        assertEquals(returnDate, borrowRecord.getReturnDate());
        assertEquals(BorrowStatus.RETURNED, borrowRecord.getBorrowStatus());
        assertEquals(BigDecimal.ZERO, borrowRecord.getPenaltyAmount());

        assertEquals(1, book.getQuantityAvailable());
        assertEquals(MembershipStatus.ACTIVE, member.getMembershipStatus());

        assertEquals(borrowRecord.getBorrowDate(), response.getBorrowDate());
        assertEquals(borrowRecord.getReturnDate(), response.getReturnDate());
        assertEquals(borrowRecord.getDueDate(), response.getDueDate());
        assertEquals(borrowRecord.getBorrowStatus(), response.getBorrowStatus());
        assertEquals(borrowRecord.getPenaltyAmount(), response.getPenaltyAmount());
    }

    @Test
    void returnBook_whenBorrowRecordNotFound_thenReturnResponse() {
        // Arrange
        long borrowRecordId = 1L;
        when(borrowRepository.findById(borrowRecordId)).thenReturn(Optional.empty());

        // Act & Assert
        BookNotAvailableException exception = assertThrows(BookNotAvailableException.class, () -> borrowService.returnBook(borrowRecordId));
        assertEquals("Borrow Record Not Found", exception.getMessage());
    }

    @Test
    void returnBook_whenDueDatePassed_thenReturnResponse() {
        // Arrange
        LocalDateTime returnDate = LocalDateTime.now();
        long borrowRecordId = 1L;
        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .publishYear(2008)
                .quantityAvailable(0)
                .category("Programming")
                .build();
        Member member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE)
                .build();
        BorrowRecord borrowRecord = BorrowRecord.builder()
                .id(borrowRecordId)
                .book(book)
                .member(member)
                .borrowDate(returnDate.minusDays(7))
                .dueDate(returnDate.minusDays(3))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE)
                .build();

        when(borrowRepository.findById(borrowRecordId)).thenReturn(Optional.of(borrowRecord));

        // Act
        ReturnBorrowResponse response = borrowService.returnBook(borrowRecordId);

        // Assert
        assertEquals(returnDate, borrowRecord.getReturnDate());
        assertEquals(BorrowStatus.OVERDUE, borrowRecord.getBorrowStatus());
        assertEquals(BigDecimal.valueOf(3), borrowRecord.getPenaltyAmount());

        assertEquals(1, book.getQuantityAvailable());
        assertEquals(MembershipStatus.ACTIVE, member.getMembershipStatus());

        assertEquals(borrowRecord.getBorrowDate(), response.getBorrowDate());
        assertEquals(borrowRecord.getReturnDate(), response.getReturnDate());
        assertEquals(borrowRecord.getDueDate(), response.getDueDate());
        assertEquals(borrowRecord.getBorrowStatus(), response.getBorrowStatus());
        assertEquals(borrowRecord.getPenaltyAmount(), response.getPenaltyAmount());
    }

}
