package com.schoolsaathi.school_managment.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MonthlyFeeDto {
    private String month;       // "April", "May"
    private Integer year;
    private BigDecimal amount;
}