package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.dto.AccountRequestDto;
import com.project.dto.AccountResponseDto;



@FeignClient(name = "springrestaccount",url = "http://localhost:8083/accounts")
public interface CustomerAccountInterface {
	  @PostMapping("/createAccount")
	    ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto request);
	  
	    @PutMapping("/deactivate/customer/{customerId}")
	    boolean deactivateAccountsByCustomer(@PathVariable("customerId") Long customerId);

}
