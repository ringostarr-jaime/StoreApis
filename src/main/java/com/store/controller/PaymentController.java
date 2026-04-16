package com.store.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.payment.PaymentDto;
import com.store.interfaces.payment.PaymentInterfaces;

import jakarta.validation.Valid;


@RestController
@RequestMapping("${payment.version}payment")
public class PaymentController {
	
	
	private final PaymentInterfaces apiService;
	
	public PaymentController(PaymentInterfaces apiService) {
		super();
		this.apiService = apiService;
	}

	@PostMapping("/createPayment")
	public ResponseEntity<Object> createPayment(@Valid @RequestBody PaymentDto pay) 
	{ 
		return apiService.createPayment(pay);
	}
	
	@GetMapping("/getStatusPay")
	public ResponseEntity<Object> createPayment(@RequestParam(name = "codeTransaction", required = true) String codeTransaction, @RequestParam(name = "idProduct", required = true) Long idProduct)  
	{ 
		return apiService.findPayment(codeTransaction,idProduct);
	}
	
}

