package com.store.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public class ClientDto {
	
	@NotNull(message = "userId may not be empty")
	@Min(value = 1, message = "the userId must be equal to or greater than 1")
	private Long userId;
	@NotEmpty(message = "date may not be empty")
	private String date;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "CartUser [userId=" + userId + ", date=" + date + "]";
	} 
	
	
}
