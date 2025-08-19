package com.erp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ErpController {

	@GetMapping("/")
	public String index()
	{
		return "index";
	}
	
	  @PostMapping("/login")
	    public String login(
	            @RequestParam("role") String role,
	            @RequestParam("username") String username,
	            @RequestParam("password") String password) {

	        // Example check
	        if ("admin".equals(role) && "admin".equals(username) && "1234".equals(password)) {
	            return "redirect:/admin-dashboard";
	        } else if ("faculty".equals(role)) {
	            return "redirect:/faculty-dashboard";
	        } else if ("student".equals(role)) {
	            return "redirect:/student-dashboard";
	        } else {
	            return "redirect:/login?error"; // invalid login
	        }
	    }
}
