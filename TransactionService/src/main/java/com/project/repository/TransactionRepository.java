package com.project.repository;

import com.project.dto.DailyTransactionCountDto;
import com.project.dto.TransactionResponseDto;
import com.project.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Match entity fields exactly
    List<Transaction> findByFromAccountNumberOrToAccountNumber(String fromAccountNumber, String toAccountNumber);

    @Query("SELECT t FROM Transaction t " +
           "WHERE t.fromAccountNumber = :accountNumber OR t.toAccountNumber = :accountNumber " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findLatestTransactions(@Param("accountNumber") String accountNumber, Pageable pageable);
    
    List<Transaction> findByFlaggedTrueOrderByCreatedAtDesc();

    @Query(value = "SELECT trunc(t.created_at) AS txnDate, COUNT(*) AS txnCount, COALESCE(SUM(t.amount), 0) AS totalAmount " +
    
            "FROM transactions t " +
            "WHERE t.created_at BETWEEN :from AND :to " +
            "GROUP BY trunc(t.created_at) " +
            "ORDER BY trunc(t.created_at)", nativeQuery = true)
List<DailyTransactionCountProjection> findDailyCounts(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

//	Optional<Transaction> findByAccountNumber(String accountNumber);

	@Query("SELECT t FROM Transaction t " +
	           "WHERE (t.fromAccountNumber = :accountNumber OR t.toAccountNumber = :accountNumber) " +
	           "AND t.createdAt BETWEEN :fromDate AND :toDate")
	    List<Transaction> findAllByAccountNumberAndDateRange(
	            @Param("accountNumber") String accountNumber,
	            @Param("fromDate") LocalDateTime fromDate,
	            @Param("toDate") LocalDateTime toDate
	    );

//	List<DailyTransactionCountProjection> findDailyCounts(Timestamp from, Timestamp to);

}


/*
 * package com.project.repository;
 * 
 * import com.project.model.Transaction; import
 * org.springframework.data.domain.Pageable; import
 * org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.data.jpa.repository.Query; import
 * org.springframework.data.repository.query.Param; import java.util.List;
 * 
 * public interface TransactionRepository extends JpaRepository<Transaction,
 * Long> {
 * 
 * List<Transaction> findByFromAccountIdOrToAccountId(String fromAccountId,
 * String toAccountId);
 * 
 * @Query("SELECT t FROM Transaction t " +
 * "WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId " +
 * "ORDER BY t.createdAt DESC") List<Transaction>
 * findLatestTransactions(@Param("accountId") String accountId, Pageable
 * pageable); }
 */