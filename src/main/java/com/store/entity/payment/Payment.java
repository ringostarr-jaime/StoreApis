package com.store.entity.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.store.entity.client.Client;
import com.store.entity.order.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;    
    private String codeTransaction;    
    private BigDecimal amount;    
    private String statusPayment;    
    private String paymentType;     
    private LocalDateTime modificationDate;    
    private LocalDateTime paymentDate;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public String getCodeTransaction() {
		return codeTransaction;
	}
	public void setCodeTransaction(String codeTransaction) {
		this.codeTransaction = codeTransaction;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getStatusPayment() {
		return statusPayment;
	}
	public void setStatusPayment(String statusPayment) {
		this.statusPayment = statusPayment;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public LocalDateTime getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(LocalDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}
	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	@Override
	public String toString() {
		return "Payment [id=" + id + ", client=" + client + ", order=" + order + ", codeTransaction=" + codeTransaction
				+ ", amount=" + amount + ", statusPayment=" + statusPayment + ", paymentType=" + paymentType
				+ ", modificationDate=" + modificationDate + ", paymentDate=" + paymentDate + "]";
	}
    
}

