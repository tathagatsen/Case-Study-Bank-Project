package com.project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.*;

@Data 
@NoArgsConstructor
@Builder
public class DailyTransactionCountDto {
    private Date date;
    private Long count;
    private BigDecimal totalAmount;
    
	public DailyTransactionCountDto(Date date, Long count, BigDecimal totalAmount) {
		super();
		this.date = date;
		this.count = count;
		this.totalAmount = totalAmount;
	}
    
    
}