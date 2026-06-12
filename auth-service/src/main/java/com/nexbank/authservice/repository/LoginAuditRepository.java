package com.nexbank.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexbank.authservice.audit.LoginAuditEntity;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAuditEntity, Long>{

}
