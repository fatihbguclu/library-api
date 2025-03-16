package com.ft.library.service.impl;

import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;
import com.ft.library.repository.BorrowRepository;
import com.ft.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;

    @Override
    public CreateBorrowResponse borrowBook(CreateBorrowRequest request) {
        return null;
    }
}
