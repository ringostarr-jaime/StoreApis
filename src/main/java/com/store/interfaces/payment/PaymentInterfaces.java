package com.store.interfaces.payment;

import org.springframework.http.ResponseEntity;

import com.store.dto.payment.PaymentDto;


public interface PaymentInterfaces {
	
	ResponseEntity<Object> createPayment(PaymentDto pay);
	ResponseEntity<Object> findPayment(String codeTransaction,Long idClient);
}
