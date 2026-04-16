package com.store.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DeleteProductDto {
	
	@NotNull(message = "orderDetailId may not be null")
	@Min(value = 1, message = "orderDetailId must be greater than or equal to 1")	
	private Long orderDetailId;

	@NotNull(message = "productId may not be null")
	@Min(value = 1, message = "productId must be greater than or equal to 1")
	private Long productId;
	
	@NotNull(message = "quantity may not be null")
	@Min(value = 1, message = "quantity must be greater than or equal to 1")
	private int quantity;

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

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

	@Override
	public String toString() {
		return "DeleteProductDto [orderDetailId=" + orderDetailId + ", productId=" + productId + ", quantity="
				+ quantity + "]";
	}
	
}
