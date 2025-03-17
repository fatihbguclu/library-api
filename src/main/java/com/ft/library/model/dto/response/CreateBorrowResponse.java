package com.ft.library.model.dto.response;

import com.ft.library.model.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class CreateBorrowResponse { // TODO : use entity instead
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private BorrowStatus borrowStatus;
}
