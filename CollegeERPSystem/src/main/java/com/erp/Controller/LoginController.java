package com.erp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.Login;
import com.erp.services.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginServ;
	@PostMapping("/login")
	public String login(@ModelAttribute Login login, Model model) {
	    Login dbUser = loginServ.getUserByName(login.getName());

	    if (dbUser == null) {
	        // Username not found → include entered name
	        model.addAttribute("name", login.getName());
	        model.addAttribute("error", "is not found");
	        return "index";
	    }

	    if (!login.getPassword().equals(dbUser.getPassword())) {
	        // Password wrong → no name
	        model.addAttribute("error", "password is wrong");
	        return "index";
	    }

	    if (!login.getRole().equals(dbUser.getRole())) {
	        // Role wrong → no name
	        model.addAttribute("error", "role is wrong");
	        return "index";
	    }

	    // ✅ Correct login
	    if ("admin".equals(dbUser.getRole())) {
	        return "admin-dashboard";
	    } else if ("faculty".equals(dbUser.getRole())) {
	        return "faculty-dashboard";
	    } else if ("student".equals(dbUser.getRole())) {
	        return "student-dashboard";
	    }

	    model.addAttribute("error", "Something went wrong");
	    return "index";
	}






}
