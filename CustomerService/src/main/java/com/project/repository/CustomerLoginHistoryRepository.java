package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Customer;
import com.project.model.CustomerLoginHistory;

public interface CustomerLoginHistoryRepository extends JpaRepository<CustomerLoginHistory, Long>{

	List<CustomerLoginHistory> findAllByCustomer(Customer customer);

}
