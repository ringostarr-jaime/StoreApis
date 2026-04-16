package com.store.interfaces.product;

import org.springframework.http.ResponseEntity;


public interface ProductsInterfaces {
	
	ResponseEntity<Object> getProduct (Long id);	
	ResponseEntity<Object> allProducts();
	
}

