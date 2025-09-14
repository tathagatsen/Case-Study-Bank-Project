package com.admin.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import java.util.*;
import com.admin.dto.AccountCountsDto;
import com.admin.dto.AccountResponseDto;
import com.admin.dto.AccountStatusChangeDto;

@FeignClient(name = "springrestaccount",url = "http://localhost:8083/accounts")
public interface AccountFeignClientInterface {
	@GetMapping("/admin/counts")
    AccountCountsDto getCounts();

    @PatchMapping("/{accountId}/status")
    ResponseEntity<AccountResponseDto> updateStatus(@PathVariable Long accountId, @RequestBody AccountStatusChangeDto dto);
    

    @GetMapping
    List<Map<String, Object>> getAllAccounts();

}
