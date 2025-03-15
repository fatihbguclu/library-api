package com.ft.library.repository;

import com.ft.library.model.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {
}
