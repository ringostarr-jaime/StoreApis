package com.store.dto.order;


import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductOrderDto {
	
	@NotNull
	@Min(value = 1, message = "the productId must be equal to or greater than 1")	
	private Long productId;	
	@Min(value = 1, message = "the quantity must be equal to or greater than 1")
	private int quantity;
	@NotNull
	@DecimalMin(value =  "0.01", message = "the value must be equal to or greater than 0.01") 
	@DecimalMax(value = "999999.99", message = "the value must be equal to or less than 999999.99")
	private BigDecimal unitPrice;	
	private BigDecimal totalPrice;	
	@NotBlank(message = "productName may not be empty")
	private String productName;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Override
	public String toString() {
		return "ProductOrder [productId=" + productId + ", quantity=" + quantity + ", unitPrice=" + unitPrice
				+ ", totalPrice=" + totalPrice + ", productName=" + productName + "]";
	}
	
}
