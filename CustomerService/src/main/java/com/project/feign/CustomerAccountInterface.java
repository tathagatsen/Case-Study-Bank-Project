package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.AccountRequestDto;



@FeignClient("Account")
public interface CustomerAccountInterface {
	@PostMapping
	public ResponseEntity<AccountRequestDto> createAccount(@RequestBody AccountRequestDto request);
}
