package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.TwoFactorAuth;
import com.project.model.UserCredential;

public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuth, Long>{

	TwoFactorAuth findByUser(UserCredential user);

}
