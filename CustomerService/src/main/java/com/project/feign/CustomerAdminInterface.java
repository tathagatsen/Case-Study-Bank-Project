package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.CustomerAdminKycDto;
import com.project.dto.CustomerKycDto;

@FeignClient(name = "admin-server", url = "http://localhost:8083", path = "/admin")
public interface CustomerAdminInterface {
    @PostMapping("/send-kyc-details")
    void sendKycDetails(@RequestBody CustomerAdminKycDto dto);

    @PostMapping("/deactivate")
    void deactivateCustomer(@RequestParam Long customerId);
}
