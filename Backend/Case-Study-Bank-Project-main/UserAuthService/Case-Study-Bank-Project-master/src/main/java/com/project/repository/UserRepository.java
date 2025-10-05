package com.project.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.UserCredential;
@Repository
public interface UserRepository extends JpaRepository<UserCredential, UUID>{

	Optional<UserCredential> findByEmail(String email);
	Optional<UserCredential> findByUserId(long userId);
	boolean existsByEmail(String email);
	boolean existsByPhoneNumber(String phoneNumber);

}
