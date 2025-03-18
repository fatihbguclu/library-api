package com.ft.library.controller.v1;

import com.ft.library.model.dto.request.CreateBorrowRequest;

import com.ft.library.model.dto.response.GenericResponse;
import com.ft.library.model.entity.BorrowEntry;
import com.ft.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping
    public ResponseEntity<GenericResponse<BorrowEntry>> borrowBook(@RequestBody CreateBorrowRequest request) {
        BorrowEntry response = borrowService.borrowBook(request);
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", response));
    }

    @PutMapping("/return/{borrowId}")
    public ResponseEntity<GenericResponse<BorrowEntry>> returnBook(@PathVariable long borrowId) {
        BorrowEntry response = borrowService.returnBook(borrowId);
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", response));
    }
}
