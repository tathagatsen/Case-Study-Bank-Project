package com.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.admin.models.AdminKyc;

public interface AdminKycRepository extends JpaRepository<AdminKyc, Long> {
    List<AdminKyc> findAllByStatus(String status);
}
