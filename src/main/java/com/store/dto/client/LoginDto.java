package com.store.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDto {
	
    @NotBlank(message = "username may not be empty")
    @Size(min = 4, max = 50, message = "username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "password may not be empty")
    @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters")
    private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginDto [username=" + username + ", password=" + password + "]";
	}
    
}

