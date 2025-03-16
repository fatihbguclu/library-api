package com.ft.library.repository;

import com.ft.library.model.entity.Book;
import com.ft.library.model.entity.BorrowRecord;
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

    private BorrowRecord borrowRecord;

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
                .publishYear(2008)
                .quantityAvailable(10)
                .category("Programming").build();

        testEntityManager.persist(member);
        testEntityManager.persist(book);

        borrowRecord = BorrowRecord.builder()
                .member(member)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(7))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE).build();

        testEntityManager.persist(borrowRecord);
    }

    @AfterEach
    void tearDown() {
        testEntityManager.clear();
    }

    @Test
    void saveBorrowRecord_shouldPersistBorrowRecord() {
        // Given
        BorrowRecord borrowRecord = BorrowRecord.builder()
                .member(member)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(7))
                .returnDate(null)
                .penaltyAmount(BigDecimal.ZERO)
                .borrowStatus(BorrowStatus.ACTIVE).build();
        // When
        BorrowRecord savedRecord = borrowRepository.save(borrowRecord);

        // Then
        BorrowRecord foundRecord = testEntityManager.find(BorrowRecord.class, savedRecord.getId());
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
        BorrowRecord foundRecord = borrowRepository.findById(borrowRecord.getId()).orElse(null);

        // Then
        assertNotNull(foundRecord);
        assertEquals(borrowRecord.getId(), foundRecord.getId());
        assertEquals(borrowRecord.getBorrowDate(), foundRecord.getBorrowDate());
        assertEquals(borrowRecord.getDueDate(), foundRecord.getDueDate());
        assertEquals(borrowRecord.getReturnDate(), foundRecord.getReturnDate());
        assertEquals(borrowRecord.getBorrowStatus(), foundRecord.getBorrowStatus());
        assertEquals(borrowRecord.getPenaltyAmount(), foundRecord.getPenaltyAmount());
        assertEquals(borrowRecord.getMember().getId(), foundRecord.getMember().getId());
        assertEquals(borrowRecord.getBook().getId(), foundRecord.getBook().getId());
    }

    @Test
    void existsByBookAndMemberAndBorrowStatus_shouldReturnTrue() {
        // When & Then
        assertTrue(borrowRepository.existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(book.getId(), member.getId(), BorrowStatus.ACTIVE));
    }

    @Test
    void existsByBookAndMemberAndBorrowStatus_whenBorrowStatusIsDifferent_shouldReturnFalse() {
        // Given
        borrowRecord.setBorrowStatus(BorrowStatus.RETURNED);
        testEntityManager.persist(borrowRecord);
        // When & Then
        assertFalse(borrowRepository.existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(book.getId(), member.getId(), BorrowStatus.ACTIVE));
    }

}
