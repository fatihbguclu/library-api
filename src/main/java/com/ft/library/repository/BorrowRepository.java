package com.ft.library.repository;

import com.ft.library.model.entity.BorrowEntry;
import com.ft.library.model.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<BorrowEntry, Long> {

    boolean existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(Long bookId, Long memberId, BorrowStatus borrowStatus);

}
