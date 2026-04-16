package com.store.dto.order;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderDto {
		
	@Valid
	@NotEmpty(message = "It must include at least one product")
	private List<ProductOrderDto> products;
	
	@NotNull(message = "userId may not be empty")
	@Min(value = 1, message = "the userId must be equal to or greater than 1")
	private Long userId;
	
	@NotEmpty(message = "shipingAdress may not be empty")	
	private String shipingAdress;
	
	private BigDecimal totalAmount;	

	public List<ProductOrderDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductOrderDto> products) {
		this.products = products;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getShipingAdress() {
		return shipingAdress;
	}

	public void setShipingAdress(String shipingAdress) {
		this.shipingAdress = shipingAdress;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "OrderDto [products=" + products + ", userId=" + userId + ", shipingAdress=" + shipingAdress
				+ ", totalAmount=" + totalAmount + "]";
	}
	
}
