package com.project.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Account;
import com.project.model.AccountStatus;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountNumber(String accountNumber);

	List<Account> findByCustomerId(Long customerId);
	long countByStatus(AccountStatus status);
	long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to); // optional
	


}
