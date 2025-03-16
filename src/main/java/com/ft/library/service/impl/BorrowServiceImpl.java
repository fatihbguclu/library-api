package com.ft.library.service.impl;

import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;
import com.ft.library.model.dto.response.ReturnBorrowResponse;
import com.ft.library.repository.BorrowRepository;
import com.ft.library.service.BookService;
import com.ft.library.service.BorrowService;
import com.ft.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;

    private final BookService bookService;

    private final MemberService memberService;

    @Override
    @Transactional
    public CreateBorrowResponse borrowBook(CreateBorrowRequest request) {
        return null;
    }

    @Override
    @Transactional
    public ReturnBorrowResponse returnBook(long borrowId) {
        return null;
    }
}
