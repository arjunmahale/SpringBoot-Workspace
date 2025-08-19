package com.thymeleafdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class HomeController {

	@GetMapping("/")
	public String index(Model model)

	{
		model.addAttribute("heading", "this is new index page");
		model.addAttribute("title", "thymeleaf page");
		return "index";
	}

	@GetMapping("/home")
	public String home()
	{
		return "homefolder/home";
	}

	@GetMapping("/form")
	public String form()
	{
		return "form";
	}



	@GetMapping("/goto/{id}")
	public String GoToHandeler(@PathVariable int id)
	{
		System.out.println("handler working");
System.out.println(id);
		return "index";
	}



}
