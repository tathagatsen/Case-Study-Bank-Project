package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.UserCustomerDto;


@FeignClient("Customer")
public interface CustomerInterface {
	@PostMapping("/register")
	public UserCustomerDto registerCustomer(@RequestBody UserCustomerDto customerDto);
	
	@PostMapping("/login")
	public Long loginCustomer(@RequestParam Long custId);
}
