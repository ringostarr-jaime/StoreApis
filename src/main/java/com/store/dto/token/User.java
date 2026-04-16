package com.store.dto.token;

public class User {
	
	private String User;	
	private String Token;

	
	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	@Override
	public String toString() {
		return "User [User=" + User + ", Token=" + Token + "]";
	}


}
