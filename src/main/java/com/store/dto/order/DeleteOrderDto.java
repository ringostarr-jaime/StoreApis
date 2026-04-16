package com.store.dto.order;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class DeleteOrderDto {
	
	@NotNull(message = "userId may not be empty")
	@Min(value = 1, message = "the userId must be equal to or greater than 1")
	private Long userId;
	
	
	@Valid	
	@NotEmpty(message = "It must include at least one product")
	private List<DeleteProductDto> orderDetailIds;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<DeleteProductDto> getOrderDetailIds() {
		return orderDetailIds;
	}

	public void setOrderDetailIds(List<DeleteProductDto> orderDetailIds) {
		this.orderDetailIds = orderDetailIds;
	}

	@Override
	public String toString() {
		return "DeleteOrderDto [userId=" + userId + ", orderDetailIds=" + orderDetailIds + "]";
	}
	
	
}
