package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Role;
import com.project.model.Role.RoleType;
import com.project.model.UserCredential;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByRoleName(String role);

	 
		// TODO Auto-generated method stub
	
}
