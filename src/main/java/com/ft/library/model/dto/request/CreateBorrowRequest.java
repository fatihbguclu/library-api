package com.ft.library.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBorrowRequest {
    private Long bookId;
    private Long memberId;
}
