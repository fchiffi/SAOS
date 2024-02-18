package com.saos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	//Controller LoginPage.html
	@GetMapping(value={"/login"})
	public String login() {

		return "Login";

	}
	
}

