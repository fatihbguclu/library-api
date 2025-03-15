package com.ft.library.controller.v1;

import com.ft.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

}
