package com.ft.library.model.dto.response;

import com.ft.library.model.enums.BorrowStatus;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@NoArgsConstructor
public class ReturnBorrowResponse {
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private BorrowStatus borrowStatus;
    private BigDecimal penaltyAmount;
}
