package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.project.dto.*;


@FeignClient(name = "admin-server", url="http://localhost:8086", path="/admin")
public interface AccountAdminClientInterface {

    @PostMapping("/accounts/created")
    void notifyAccountCreated(@RequestBody AccountCreatedDto dto);

    @PostMapping("/accounts/status-change")
    void notifyAccountStatusChange(@RequestBody AccountStatusChangeDto dto);

    // optionally Admin can call account for counts; or Admin calls account endpoints directly
    @GetMapping("/accounts/active-count")
    Long getActiveAccountCount();
}