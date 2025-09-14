package com.admin.controller;

import com.admin.services.DeactivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//// MAPPING /deactivation
//@RequestMapping("/deactivation")
//public class DeactivationController {
//
//    private final DeactivationService service;
//
//    public DeactivationController(DeactivationService service) {
//        this.service = service;
//    }
//
//    // POST /{userId}
//    @PostMapping("/{userId}")
//    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
//        service.deactivate(userId);
//        return ResponseEntity.ok("User deactivated successfully");
//    }
//}


@RestController
// MAPPING /deactivation
@RequestMapping("/deactivation")
public class DeactivationController {

    
    // POST /{userId}
    @PostMapping("/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
        service.deactivateCustomer(userId);
        return ResponseEntity.ok("User deactivated successfully");
    }
    
    private final DeactivationService service;

    public DeactivationController(DeactivationService service) {
        this.service = service;
    }

    @PutMapping("/{customerId}")
    public boolean deactivateCustomer(@PathVariable Long customerId) {
        boolean b= service.deactivateCustomer(customerId);
//        return ResponseEntity.ok("Customer " + customerId + " deactivated successfully");
        return b;
    }
}

/*
 * package com.admin.controller;
 * 
 * import com.admin.services.DeactivationService; import
 * org.springframework.web.bind.annotation.*;
 * 
 * @RestController
 * 
 * @RequestMapping("/admin/deactivate") public class DeactivationController {
 * 
 * private final DeactivationService service;
 * 
 * public DeactivationController(DeactivationService service) { this.service =
 * service; }
 * 
 * @PostMapping("/users/{id}") public String deactivateUser(@PathVariable Long
 * id) { return service.deactivateUser(id); } }
 */