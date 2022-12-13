package com.smohan.opaauthorization.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpaAuthorizationController {

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello World";
	}

	@GetMapping("/bye")
	public String sayBye() {
		return "Bye";
	}
}
