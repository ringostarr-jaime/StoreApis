package com.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.order.DeleteOrderDto;
import com.store.dto.order.OrderDto;
import com.store.interfaces.order.OrderInterfaces;

import jakarta.validation.Valid;


@RestController
@RequestMapping("${order.version}orders")
public class OrderController  {
	
	
	private final OrderInterfaces apiService;
	
	public OrderController(OrderInterfaces apiService) {
		super();
		this.apiService = apiService;
	}

	@PostMapping("/addProductCart")
	public ResponseEntity<Object> addProductCart(@Valid @RequestBody OrderDto order)
	{ 
		return apiService.addProductCart(order);
	}
	
	@GetMapping("/getOrder")
	public ResponseEntity<Object> getOrder(@RequestParam(name= "username", required = true) String username) 
	{ 
		return apiService.getOrder(username);
	}
	
	
	@PutMapping("/adjustOrderDetails")
	public ResponseEntity<Object> adjustOrderDetails(@Valid @RequestBody DeleteOrderDto orderDto) {
	    return apiService.adjustOrderDetails(orderDto);
	}

	
	@DeleteMapping("/deleteOrder/{idOrder}")
	public ResponseEntity<Object> deleteOrder(@PathVariable(name = "idOrder", required = true) Long idOrder)
	{ 
		return apiService.deleteOrder(idOrder);
	}
	
	
}
