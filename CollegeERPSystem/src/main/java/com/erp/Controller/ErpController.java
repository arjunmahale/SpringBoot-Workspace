package com.erp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ErpController {

	@GetMapping("/")
	public String index()
	{
		return "index";
	}
}
