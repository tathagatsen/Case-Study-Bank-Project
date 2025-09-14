package com.project.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.AccountLock;
import com.project.model.UserCredential;
@Repository
public interface AccountLockRepository extends JpaRepository<AccountLock, Long>{

	AccountLock findByUser(UserCredential user);

}
