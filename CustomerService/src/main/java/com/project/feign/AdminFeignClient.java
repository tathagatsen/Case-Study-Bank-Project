package com.project.feign;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.dto.CustomerAdminKycDto;


@FeignClient(name = "admin-service", url = "http://localhost:8086/admin") 
public interface AdminFeignClient {
	@PostMapping("/send-kyc-details")
    public ResponseEntity<Void> receiveKyc(@RequestBody CustomerAdminKycDto dto);
}
