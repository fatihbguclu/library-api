package com.ft.library.service.impl;

import com.ft.library.repository.BorrowRepository;
import com.ft.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;

}
