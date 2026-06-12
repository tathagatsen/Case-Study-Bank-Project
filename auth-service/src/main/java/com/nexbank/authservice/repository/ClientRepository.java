package com.nexbank.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexbank.authservice.domain.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
	Optional<Client> FindByClientId(String clientId);
}
