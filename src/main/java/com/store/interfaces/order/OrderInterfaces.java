package com.store.interfaces.order;

import org.springframework.http.ResponseEntity;

import com.store.dto.order.ClientDto;
import com.store.dto.order.DeleteOrderDto;
import com.store.dto.order.OrderDto;


public interface OrderInterfaces {
	
	ResponseEntity<Object> findByUserIdAndStatus (Long id,String status);
	ResponseEntity<Object> addProductCart (OrderDto order);
	ResponseEntity<Object> getOrder (String username);
	ResponseEntity<Object> adjustOrderDetails (DeleteOrderDto order);
	ResponseEntity<Object> deleteOrder (Long idOrder);
	
}
