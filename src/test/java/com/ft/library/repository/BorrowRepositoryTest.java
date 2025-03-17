package com.ft.library.repository;

import com.ft.library.model.entity.Book;
import com.ft.library.model.entity.BorrowEntry;
import com.ft.library.model.entity.Member;
import com.ft.library.model.enums.BorrowStatus;
import com.ft.library.model.enums.MembershipStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class BorrowRepositoryTest {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;

    private Book book;

    private BorrowEntry borrowEntry;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .firstName("Fatih")
                .lastName("Büyükgüçlü")
                .email("fatih@gmail.com")
                .membershipStatus(MembershipStatus.ACTIVE).build();

        book = Book.builder()
                .title("Clean Code")
                .isbn("9780132350884")
                .author("Robert C. Martin")
                .quantityAvailable(10).build();

        testEntityManager.persist(member);
        testEntityManager.persist(book);

        borrowEntry = BorrowEntry.builder()
                .member(member)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(7))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE).build();

        testEntityManager.persist(borrowEntry);
    }

    @AfterEach
    void tearDown() {
        testEntityManager.clear();
    }

    @Test
    void saveBorrowRecord_shouldPersistBorrowRecord() {
        // Given
        BorrowEntry borrowEntry = BorrowEntry.builder()
                .member(member)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(7))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE).build();
        // When
        BorrowEntry savedRecord = borrowRepository.save(borrowEntry);

        // Then
        BorrowEntry foundRecord = testEntityManager.find(BorrowEntry.class, savedRecord.getId());
        assertNotNull(foundRecord);
        assertEquals(foundRecord.getBorrowDate(), savedRecord.getBorrowDate());
        assertEquals(foundRecord.getDueDate(), savedRecord.getDueDate());
        assertNull(foundRecord.getReturnDate());
        assertEquals(foundRecord.getBorrowStatus(), savedRecord.getBorrowStatus());
        assertEquals(foundRecord.getPenaltyAmount(), savedRecord.getPenaltyAmount());

        assertEquals(foundRecord.getMember().getId(), savedRecord.getMember().getId());
        assertEquals(foundRecord.getBook().getId(), savedRecord.getBook().getId());
    }

    @Test
    void findBorrowRecordById_shouldReturnBorrowRecord() {
        // When
        BorrowEntry foundRecord = borrowRepository.findById(borrowEntry.getId()).orElse(null);

        // Then
        assertNotNull(foundRecord);
        assertEquals(borrowEntry.getId(), foundRecord.getId());
        assertEquals(borrowEntry.getBorrowDate(), foundRecord.getBorrowDate());
        assertEquals(borrowEntry.getDueDate(), foundRecord.getDueDate());
        assertEquals(borrowEntry.getReturnDate(), foundRecord.getReturnDate());
        assertEquals(borrowEntry.getBorrowStatus(), foundRecord.getBorrowStatus());
        assertEquals(borrowEntry.getPenaltyAmount(), foundRecord.getPenaltyAmount());
        assertEquals(borrowEntry.getMember().getId(), foundRecord.getMember().getId());
        assertEquals(borrowEntry.getBook().getId(), foundRecord.getBook().getId());
    }

    @Test
    void existsByBookAndMemberAndBorrowStatus_shouldReturnTrue() {
        // When & Then
        assertTrue(borrowRepository.existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(book.getId(), member.getId(), BorrowStatus.ACTIVE));
    }

    @Test
    void existsByBookAndMemberAndBorrowStatus_whenBorrowStatusIsDifferent_shouldReturnFalse() {
        // Given
        borrowEntry.setBorrowStatus(BorrowStatus.RETURNED);
        testEntityManager.persist(borrowEntry);
        // When & Then
        assertFalse(borrowRepository.existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(book.getId(), member.getId(), BorrowStatus.ACTIVE));
    }

}
