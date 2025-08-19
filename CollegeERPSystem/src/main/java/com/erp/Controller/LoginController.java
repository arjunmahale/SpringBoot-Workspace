package com.erp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.erp.model.Login;
import com.erp.services.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginServ;
	@PostMapping("/login")
	public String login(@ModelAttribute Login login, Model model) {

	    Login dbUser = loginServ.getUserByName(login.getName());

	    if (dbUser != null
	        && login.getPassword().equals(dbUser.getPassword())
	        && login.getRole().equals(dbUser.getRole())) {

	        // Now branch by role
	        if ("admin".equals(dbUser.getRole())) {
	            return "admin-dashboard";
	        } else if ("faculty".equals(dbUser.getRole())) {
	            return "faculty-dashboard";
	        } else if ("student".equals(dbUser.getRole())) {
	            return "student-dashboard";
	        }
	    }

	    // Invalid login
	    model.addAttribute("error", "Invalid credentials");
	    return "index";
	}

}
