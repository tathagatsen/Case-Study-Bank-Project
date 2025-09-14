package com.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.admin.models.AdminActivityLog;

@Repository
public interface AdminActivityRepository extends JpaRepository<AdminActivityLog, Long> {
    List<AdminActivityLog> findTop100ByOrderByTimestampDesc();
}