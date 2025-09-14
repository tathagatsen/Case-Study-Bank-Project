package com.admin.controller;

import com.admin.services.RbacService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//// MAPPING /rbac
//@RequestMapping("/rbac")
//public class RbacController {
//
//    private final RbacService service;
//
//    public RbacController(RbacService service) {
//        this.service = service;
//    }
//
//    // POST /assign-role/{userId}/{role}
//    @PostMapping("/assign-role/{userId}/{role}")
//    public ResponseEntity<String> assignRole(@PathVariable Long userId, @PathVariable String role) {
//        service.assignRole(userId, role);
//        return ResponseEntity.ok("Role assigned successfully");
//    }
//
//    // GET /roles/{userId}
//    @GetMapping("/roles/{userId}")
//    public ResponseEntity<String> getUserRoles(@PathVariable Long userId) {
//        return ResponseEntity.ok(service.getUserRoles(userId));
//    }
//}
//
//
//
///*
// * package com.admin.controller;
// * 
// * import com.admin.models.Role; import com.admin.services.RbacService; import
// * org.springframework.web.bind.annotation.*;
// * 
// * import java.util.List;
// * 
// * @RestController
// * 
// * @RequestMapping("/admin/rbac") public class RbacController {
// * 
// * private final RbacService service;
// * 
// * public RbacController(RbacService service) { this.service = service; }
// * 
// * @GetMapping("/users/{id}/roles") public List<Role> getRoles(@PathVariable
// * Long id) { return service.getRoles(id); }
// * 
// * @PostMapping("/users/{id}/roles") public List<Role> assignRole(@PathVariable
// * Long id, @RequestBody Role role) { return service.assignRole(id, role); } }
// */
@RestController
//MAPPING /rbac
@RequestMapping("/rbac")
public class RbacController {

 private final RbacService service;

 public RbacController(RbacService service) {
     this.service = service;
 }

 // POST /assign-role/{userId}/{role}
 @PostMapping("/assign-role/{userId}/{role}")
 public ResponseEntity<String> assignRole(@PathVariable Long userId, @PathVariable String role) {
     service.assignRole(userId, role);
     return ResponseEntity.ok("Role assigned successfully");
 }

 // GET /roles/{userId}
 @GetMapping("/roles/{userId}")
 public ResponseEntity<String> getUserRoles(@PathVariable Long userId) {
     return ResponseEntity.ok(service.getUserRoles(userId));
 }
}