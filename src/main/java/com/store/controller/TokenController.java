package com.store.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.client.LoginDto;
import com.store.implement.token.TokenServices;

import jakarta.validation.Valid;

@RestController
public class TokenController {	
	
	private final TokenServices tokenServices;	
	
	
	public TokenController(TokenServices tokenServices) {
		super();
		this.tokenServices = tokenServices;
	}

	@PostMapping("/token")
	public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto) {
	    return tokenServices.login(loginDto);
	}
	

}

