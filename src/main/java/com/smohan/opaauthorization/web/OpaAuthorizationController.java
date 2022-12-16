package com.smohan.opaauthorization.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpaAuthorizationController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/hello")
	public String sayHello() {
		LOGGER.info("Controller: Hello World");
		return "Hello World";
	}

	@GetMapping("/bye")
	public String sayBye() {
		return "Bye";
	}
}
