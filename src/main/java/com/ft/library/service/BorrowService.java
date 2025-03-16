package com.ft.library.service;

import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;
import com.ft.library.model.dto.response.ReturnBorrowResponse;

public interface BorrowService {
    CreateBorrowResponse borrowBook(CreateBorrowRequest request);

    ReturnBorrowResponse returnBook(long borrowId);
}
