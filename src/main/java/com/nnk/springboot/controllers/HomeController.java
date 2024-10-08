package com.nnk.springboot.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	@RequestMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/home";
	}

	@GetMapping("/error")
	public String error(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth != null ? auth.getName() : "Anonymous";
		model.addAttribute("errorMsg", "You do not have permission to access this page.");
		model.addAttribute("username", username);
		return "403";
	}
}
