//package com.project.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.project.model.TemporaryToken;
//import com.project.model.TemporaryToken.Purpose;
//import com.project.model.UserCredential;
//@Repository
//public interface TemporaryTokenRepository extends JpaRepository<TemporaryToken, Long>{
//
//	TemporaryToken findByUser(UserCredential user);
//
//	Optional<TemporaryToken> findTopByUserAndPurposeOrderByExpiryTimeDesc(UserCredential user, TemporaryToken.Purpose passwordReset);
//
//}
