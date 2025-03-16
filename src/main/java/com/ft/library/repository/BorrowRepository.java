package com.ft.library.repository;

import com.ft.library.model.entity.BorrowRecord;
import com.ft.library.model.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {

    boolean existsBorrowRecordByBookIdAndMemberIdAndBorrowStatus(Long bookId, Long memberId, BorrowStatus borrowStatus);

}
