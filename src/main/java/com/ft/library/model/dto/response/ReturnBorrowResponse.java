package com.ft.library.model.dto.response;

import com.ft.library.model.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ReturnBorrowResponse {
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private LocalDateTime dueDate;
    private BorrowStatus borrowStatus;
    private BigDecimal penaltyAmount;
}
