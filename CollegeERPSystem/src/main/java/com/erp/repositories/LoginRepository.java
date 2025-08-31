package com.erp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	Login findByName(String name);

	


	boolean existsByNameAndEmail(String name, String email);




}
