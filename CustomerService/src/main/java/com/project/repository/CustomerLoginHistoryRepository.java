package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Customer;
import com.project.model.CustomerLoginHistory;
@Repository
public interface CustomerLoginHistoryRepository extends JpaRepository<CustomerLoginHistory, Long>{

	List<CustomerLoginHistory> findAllByCustomer(Customer customer);

}
