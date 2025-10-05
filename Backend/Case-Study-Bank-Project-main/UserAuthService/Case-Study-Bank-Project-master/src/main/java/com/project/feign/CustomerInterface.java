package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.CustomerLoginDto;
import com.project.dto.UserCustomerDto;

import jakarta.servlet.http.HttpServletRequest;


@FeignClient(name = "Customer",url  = "http://localhost:8082/customer")
public interface CustomerInterface {
	@PostMapping("/register")
	public ResponseEntity<UserCustomerDto> registerCustomer(@RequestBody UserCustomerDto customerDto);
	
	@PostMapping("/login")
	public ResponseEntity<Long> loginCustomer(@RequestBody CustomerLoginDto loginDto);
}
