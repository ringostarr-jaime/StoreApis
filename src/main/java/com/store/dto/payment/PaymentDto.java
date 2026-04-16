package com.store.dto.payment;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PaymentDto {
	
	@NotNull(message = "userId may not be empty")
	@Min(value = 1, message = "the userId must be equal to or less than 1")
	@Max(value = 9999999, message = "the userId must be equal to or less than 9999999")
	private Long userId;	
	@NotNull(message = "mount may not be empty")
	@DecimalMin(value =  "0.01", message = "the mount must be equal to or greater than 0.01") 
	@DecimalMax(value = "999999.99", message = "the mount must be equal to or less than 999999.99")
	private BigDecimal amount;	
	private UUID idTransaction;
	private String paymentType;
	private String paymentStatus;
	private Long idOrder;
	private LocalDateTime paymentDate;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public UUID getIdTransaction() {
		return idTransaction;
	}
	public void setIdTransaction(UUID idTransaction) {
		this.idTransaction = idTransaction;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Long getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(Long idOrder) {
		this.idOrder = idOrder;
	}	
	
	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	@Override
	public String toString() {
		return "PaymentDto [userId=" + userId + ", amount=" + amount + ", idTransaction=" + idTransaction
				+ ", paymentType=" + paymentType + ", paymentStatus=" + paymentStatus + ", idOrder=" + idOrder
				+ ", paymentDate=" + paymentDate + "]";
	}
	
}

