package com.admin.services;

import com.admin.dto.AdminRequestDto;
import com.admin.models.Admin;
import com.admin.models.RoleType;
import com.admin.repositories.AdminUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserServices {

    private final AdminUserRepository repository;
    
//	@Autowired
//	private PasswordEncoder passwordEncoder;	

    public AdminUserServices(AdminUserRepository repository) {
        this.repository = repository;
    }

    public List<Admin> getAllUsers() {
    	 return repository.findAll();
       
        
    }

    public Optional<Admin> getUserById(Long id) {
 
    		return repository.findById(id);
    }

//    public AdminRequestDto createUser(AdminRequestDto adminRequestDto) {
//    	Admin admin=new Admin();
//		admin.setEmail(adminRequestDto.getEmail());
//		admin.setUsername(adminRequestDto.getFirstName().substring(0,3)+adminRequestDto.getLastName().substring(0,3));
//		 admin.setRole(RoleType.ADMIN.toString());
//		 repository.save(admin);
//		 return adminRequestDto;
//    }

    public Admin updateUser(Long id, Admin updatedUser) {
        return repository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setRole(updatedUser.getRole());
                   
                    return repository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

	public Admin createUser(AdminRequestDto admin) {
		// TODO Auto-generated method stub
		Admin a=new Admin();
		a.setEmail(admin.getEmail());
		a.setUsername(admin.getFirstName());
		 a.setRole(RoleType.ADMIN.toString());
		 a.setPassword(admin.getPassword());
		 repository.save(a);
		 return a;
//		return null;
	}
}


/*
 * package com.admin.services;
 * 
 * import com.admin.models.Admin; import
 * com.admin.repositories.AdminUserRepository; import
 * org.springframework.stereotype.Service;
 * 
 * import java.util.List; import java.util.Optional;
 * 
 * @Service public class AdminUserServices {
 * 
 * private final AdminUserRepository repository;
 * 
 * public AdminUserServices(AdminUserRepository repository) { this.repository =
 * repository; }
 * 
 * public List<Admin> getAllUsers() { return repository.findAll(); }
 * 
 * public Optional<Admin> getUserById(Long id) { return repository.findById(id);
 * }
 * 
 * public Admin createUser(Admin user) { return repository.save(user); }
 * 
 * public void deleteUser(Long id) { repository.deleteById(id); } }
 */