package com.nexbank.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexbank.authservice.domain.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUserName(String username);
}
