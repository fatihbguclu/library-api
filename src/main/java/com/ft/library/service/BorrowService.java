package com.ft.library.service;

import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.entity.BorrowEntry;

public interface BorrowService {
    BorrowEntry borrowBook(CreateBorrowRequest request);

    BorrowEntry returnBook(long borrowId);
}
