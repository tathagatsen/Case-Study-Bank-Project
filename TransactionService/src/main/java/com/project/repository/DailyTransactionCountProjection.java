package com.project.repository;

import java.math.BigDecimal;
import java.util.Date; // Change to LocalDate if your DB/JPA mapping supports it

public interface DailyTransactionCountProjection {
    Date getTxnDate();
    Long getTxnCount();
    BigDecimal getTotalAmount();
}