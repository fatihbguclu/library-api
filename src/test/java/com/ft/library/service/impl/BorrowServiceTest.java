package com.ft.library.service.impl;

import com.ft.library.exception.BookNotAvailableException;
import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;
import com.ft.library.model.dto.response.ReturnBorrowResponse;
import com.ft.library.model.entity.Book;
import com.ft.library.model.entity.BorrowEntry;
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
                .quantityAvailable(10)
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
        assertEquals(9, book.getQuantityAvailable());
        //assertEquals(borrowDate, response.getBorrowDate()); // TODO : LocalDateTime.now() wont match
        //assertEquals(borrowDate.plusDays(7), response.getDueDate()); // TODO : LocalDateTime.now() wont match
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
        assertEquals("Book is Already Borrowed by Member", exception.getMessage());
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
                .quantityAvailable(0)
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
        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .quantityAvailable(10)
                .build();
        Member member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.SUSPENDED)
                .build();
        when(memberService.getMemberById(1L)).thenReturn(member);
        when(bookService.getBookById(1L)).thenReturn(book);

        // Act & Assert
        BookNotAvailableException exception = assertThrows(BookNotAvailableException.class, () -> borrowService.borrowBook(request));
        assertEquals("Member Status is Suspended", exception.getMessage());
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
                .quantityAvailable(0)
                .build();
        Member member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE)
                .build();
        BorrowEntry borrowEntry = BorrowEntry.builder()
                .id(borrowRecordId)
                .book(book)
                .member(member)
                .borrowDate(returnDate.minusDays(7))
                .dueDate(returnDate.plusDays(1))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE)
                .build();

        when(borrowRepository.findById(borrowRecordId)).thenReturn(Optional.of(borrowEntry));

        // Act
        ReturnBorrowResponse response = borrowService.returnBook(borrowRecordId);

        // Assert
        assertEquals(returnDate, borrowEntry.getReturnDate());
        assertEquals(BorrowStatus.RETURNED, borrowEntry.getBorrowStatus());
        assertEquals(BigDecimal.ZERO, borrowEntry.getPenaltyAmount());

        assertEquals(1, book.getQuantityAvailable());
        assertEquals(MembershipStatus.ACTIVE, member.getMembershipStatus());

        assertEquals(borrowEntry.getBorrowDate(), response.getBorrowDate());
        assertEquals(borrowEntry.getReturnDate(), response.getReturnDate());
        assertEquals(borrowEntry.getDueDate(), response.getDueDate());
        assertEquals(borrowEntry.getBorrowStatus(), response.getBorrowStatus());
        assertEquals(borrowEntry.getPenaltyAmount(), response.getPenaltyAmount());
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
                .quantityAvailable(0)
                .build();
        Member member = Member.builder()
                .id(1L)
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE)
                .build();
        BorrowEntry borrowEntry = BorrowEntry.builder()
                .id(borrowRecordId)
                .book(book)
                .member(member)
                .borrowDate(returnDate.minusDays(7))
                .dueDate(returnDate.minusDays(3))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE)
                .build();

        when(borrowRepository.findById(borrowRecordId)).thenReturn(Optional.of(borrowEntry));

        // Act
        ReturnBorrowResponse response = borrowService.returnBook(borrowRecordId);

        // Assert
        //assertEquals(returnDate, borrowEntry.getReturnDate()); // TODO : LocalDateTime.now() wont match
        assertEquals(BorrowStatus.OVERDUE, borrowEntry.getBorrowStatus());
        assertEquals(BigDecimal.valueOf(3), borrowEntry.getPenaltyAmount());

        assertEquals(1, book.getQuantityAvailable());
        assertEquals(MembershipStatus.ACTIVE, member.getMembershipStatus());

        assertEquals(borrowEntry.getBorrowDate(), response.getBorrowDate());
        assertEquals(borrowEntry.getReturnDate(), response.getReturnDate());
        assertEquals(borrowEntry.getDueDate(), response.getDueDate());
        assertEquals(borrowEntry.getBorrowStatus(), response.getBorrowStatus());
        assertEquals(borrowEntry.getPenaltyAmount(), response.getPenaltyAmount());
    }

}
