package com.chainsys.examease.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

	
 @RequestMapping ("/")
 public String home() {
	 System.out.println("in landing page");
	 return "landingPage.jsp";
	 } 
}
