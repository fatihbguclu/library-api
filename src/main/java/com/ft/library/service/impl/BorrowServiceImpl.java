package com.ft.library.service.impl;

import com.ft.library.exception.BookNotAvailableException;
import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.entity.Book;
import com.ft.library.model.entity.BorrowEntry;
import com.ft.library.model.entity.Member;
import com.ft.library.model.enums.BorrowStatus;
import com.ft.library.model.enums.MembershipStatus;
import com.ft.library.repository.BorrowRepository;
import com.ft.library.service.BookService;
import com.ft.library.service.BorrowService;
import com.ft.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;

    private final BookService bookService;

    private final MemberService memberService;

    @Override
    @Transactional
    public BorrowEntry borrowBook(CreateBorrowRequest request) {
        boolean isBookAlreadyBorrowedByMember = borrowRepository.existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(
                request.getBookId(), request.getMemberId(), BorrowStatus.ACTIVE
        );
        if (isBookAlreadyBorrowedByMember) {
            throw new BookNotAvailableException("Book is Already Borrowed by Member");
        }

        Book requestedBook = bookService.getBookById(request.getBookId());
        if (requestedBook.getQuantityAvailable().compareTo(0) <= 0) {
            throw new BookNotAvailableException("Book Stock Not Available");
        }

        Member requesterMember = memberService.getMemberById(request.getMemberId());
        if (MembershipStatus.SUSPENDED.equals(requesterMember.getMembershipStatus())) {
            throw new BookNotAvailableException("Member Status is Suspended");
        }

        requestedBook.setQuantityAvailable(requestedBook.getQuantityAvailable() - 1);
        LocalDateTime now = LocalDateTime.now();
        BorrowEntry borrowEntry = BorrowEntry.builder()
                .book(requestedBook)
                .member(requesterMember)
                .borrowDate(now)
                .dueDate(now.plusDays(7))
                .returnDate(null)
                .borrowStatus(BorrowStatus.ACTIVE)
                .penaltyAmount(BigDecimal.ZERO)
                .build();
        borrowRepository.save(borrowEntry);
        return borrowEntry;
    }

    @Override
    @Transactional
    public BorrowEntry returnBook(long borrowId) {
        BorrowEntry foundBorrowRecord = borrowRepository.findById(borrowId).orElseThrow(() -> new BookNotAvailableException("Borrow Record Not Found"));

        LocalDateTime now = LocalDateTime.now();
        if (foundBorrowRecord.getDueDate().isBefore(now)) {
            long daysOverdue = foundBorrowRecord.getDueDate().until(LocalDateTime.now(), ChronoUnit.DAYS);
            foundBorrowRecord.setPenaltyAmount(BigDecimal.valueOf(daysOverdue));
            foundBorrowRecord.setBorrowStatus(BorrowStatus.OVERDUE);
        } else {
            foundBorrowRecord.setBorrowStatus(BorrowStatus.RETURNED);
        }

        foundBorrowRecord.getBook().setQuantityAvailable(foundBorrowRecord.getBook().getQuantityAvailable() + 1);
        foundBorrowRecord.setReturnDate(LocalDateTime.now());
        borrowRepository.save(foundBorrowRecord);
        return foundBorrowRecord;
    }

    /*
    @Override
    @Transactional
    public BorrowEntry borrowBook(CreateBorrowRequest request) {
        LocalDateTime now = LocalDateTime.now();
        BorrowEntry borrowEntry = BorrowEntry.builder()
                .borrowDate(now)
                .dueDate(now.plusDays(7))
                .returnDate(null)
                .borrowStatus(BorrowStatus.ACTIVE)
                .penaltyAmount(BigDecimal.ZERO)
                .build();
        borrowRepository.save(borrowEntry);
        return borrowEntry;
    }
    */

    /*
    @Override
    @Transactional
    public BorrowEntry borrowBook(CreateBorrowRequest request) {
        Book requestedBook = bookService.getBookById(request.getBookId());
        if (requestedBook.getQuantityAvailable().compareTo(0) <= 0) {
            throw new BookNotAvailableException("Book Stock Not Available");
        }
        requestedBook.setQuantityAvailable(requestedBook.getQuantityAvailable() - 1);

        LocalDateTime now = LocalDateTime.now();
        BorrowEntry borrowEntry = BorrowEntry.builder()
                .book(requestedBook)
                .borrowDate(now)
                .dueDate(now.plusDays(7))
                .returnDate(null)
                .borrowStatus(BorrowStatus.ACTIVE)
                .penaltyAmount(BigDecimal.ZERO)
                .build();
        borrowRepository.save(borrowEntry);
        return borrowEntry;
    }
    */

    /*
    @Override
    @Transactional
    public BorrowEntry borrowBook(CreateBorrowRequest request) {
        boolean isBookAlreadyBorrowedByMember = borrowRepository.existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(
                request.getBookId(), request.getMemberId(), BorrowStatus.ACTIVE
        );
        if (isBookAlreadyBorrowedByMember) {
            throw new BookNotAvailableException("Book is Already Borrowed by Member");
        }

        Book requestedBook = bookService.getBookById(request.getBookId());
        if (requestedBook.getQuantityAvailable().compareTo(0) <= 0) {
            throw new BookNotAvailableException("Book Stock Not Available");
        }

        Member requesterMember = memberService.getMemberById(request.getMemberId());
        if (MembershipStatus.SUSPENDED.equals(requesterMember.getMembershipStatus())) {
            throw new BookNotAvailableException("Member Status is Suspended");
        }

        requestedBook.setQuantityAvailable(requestedBook.getQuantityAvailable() - 1);
        LocalDateTime now = LocalDateTime.now();
        BorrowEntry borrowEntry = BorrowEntry.builder()
                .book(requestedBook)
                .member(requesterMember)
                .borrowDate(now)
                .dueDate(now.plusDays(7))
                .returnDate(null)
                .borrowStatus(BorrowStatus.ACTIVE)
                .penaltyAmount(BigDecimal.ZERO)
                .build();
        borrowRepository.save(borrowEntry);
        return borrowEntry;
    }
    */

}
