package com.ft.library.controller.v1;

import com.ft.library.model.dto.request.CreateBorrowRequest;
import com.ft.library.model.dto.response.CreateBorrowResponse;
import com.ft.library.model.dto.response.GenericResponse;
import com.ft.library.model.dto.response.ReturnBorrowResponse;
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
    public ResponseEntity<GenericResponse<CreateBorrowResponse>> borrowBook(@RequestBody CreateBorrowRequest request) {
        CreateBorrowResponse response = borrowService.borrowBook(request);
        return ResponseEntity.ok(GenericResponse.of("Success", "Success", response));
    }

    @PutMapping("/return/{borrowId}")
    public ResponseEntity<GenericResponse<ReturnBorrowResponse>> returnBook(@PathVariable long borrowId) {
        return null;
    }
}
