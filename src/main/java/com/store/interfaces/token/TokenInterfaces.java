package com.store.interfaces.token;

import org.springframework.http.ResponseEntity;

import com.store.dto.client.LoginDto;

public interface TokenInterfaces {
 
	ResponseEntity<Object> login( LoginDto loginDto);
	String getJWTToken(String username);
}
