package com.store.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.implement.product.ProductsService;
import com.store.utilidades.ErrorExceptionHandler;


@RestController
@RequestMapping("${products.version}products")
public class ProductsController extends ErrorExceptionHandler {
	
	
	private final ProductsService apiService;	

	
	public ProductsController(ProductsService apiService) {
		super();
		this.apiService = apiService;
	}

	@GetMapping("/singleProduct")
	public ResponseEntity<Object> getProduct(@RequestParam(name = "idProduct", required = true) Long idProduct) 
	{ 
		return apiService.getProduct(idProduct);
	}
	
	@GetMapping("/allProducts")
	public ResponseEntity<Object> allProducts() 
	{ 
		return apiService.allProducts();
	}	
	
}

