package com.admin.models;

import java.time.LocalDateTime;

import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@NoRepositoryBean
public class AdminCustomer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long customerId;
	
	private LocalDateTime createdAt;
}
