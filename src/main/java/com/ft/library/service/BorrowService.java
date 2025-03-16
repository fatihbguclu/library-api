package com.ft.library.service;

import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;

public interface BorrowService {
    CreateBorrowResponse borrowBook(CreateBorrowRequest request);
}
