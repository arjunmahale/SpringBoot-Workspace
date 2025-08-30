package com.erp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Login;
import com.erp.model.Student;
import com.erp.repositories.LoginRepository;

@Service
public class LoginService {
	 @Autowired
	    private LoginRepository loginRepo;

	    public Login getUserByName(String username) {
	        return loginRepo.findByName(username); // returns null if not found
	    }
	    
	    public Login saveLogin(Login login) {
			
	    	if (!loginRepo.existsByNameAndEmail(login.getName(), login.getEmail())) {
	    	    return loginRepo.save(login);
	    	}
			return login;

		}
	   
}
