package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.CustomerAdminKycDto;
import com.project.dto.CustomerKycDto;

@FeignClient("/ADMIN")
public interface CustomerAdminInterface {
	@PostMapping("/send-kyc-details")
	public void sendKycDetails(@RequestBody CustomerAdminKycDto customerAdminKycDto);
}
