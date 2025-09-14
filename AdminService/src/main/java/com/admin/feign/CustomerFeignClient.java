package com.admin.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.admin.dto.CustomerCountDto;
import com.admin.dto.CustomerKycStatusUpdateDto;

@FeignClient(name = "Customer", url = "http://localhost:8082", path = "/customer")
public interface CustomerFeignClient {
//    @PostMapping("/admin/kyc/status")
//    void updateKycStatus(@RequestBody CustomerKycStatusUpdateDto dto);
    @GetMapping("/counts")
    CustomerCountDto getUserCounts();
//    @GetMapping("/login-history")
//   Map<String,Object>> getLoginHistory(@RequestParam(required=false) Long customerId, @RequestParam(required=false) String from, @RequestParam(required=false) String to);
//    @GetMapping("/login-history")
//	List<Map<String, Object>> getLoginHistory(Long customerId, String from, String to);
//	
    @PutMapping("/kyc/status")
    void updateKycStatus(@RequestBody CustomerKycStatusUpdateDto dto);
    
    @PutMapping("/deactivate/{customerId}")
	boolean deactivateCustomer(@PathVariable("customerId") Long customerId);


}
