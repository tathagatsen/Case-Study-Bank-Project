package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
}
