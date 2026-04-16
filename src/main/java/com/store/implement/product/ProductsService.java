package com.store.implement.product;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.store.interfaces.product.ProductsInterfaces;
import com.store.utilidades.Constantes;
import com.store.utilidades.Utilidades;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;


@Service
public class ProductsService implements ProductsInterfaces{

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Override
	@CircuitBreaker(name = "globalService", fallbackMethod = "fallbackGlobalService")
	@Retry(name = "globalService")
	public ResponseEntity<Object> getProduct(Long id) {
		ResponseEntity<Object> responseEntity = null;
		String url = Constantes.URL_PRODUCTS+"/"+id;
		responseEntity  = restTemplate.getForEntity(url, Object.class); 		
		return responseEntity;
	}

	@Override
	@CircuitBreaker(name = "globalService", fallbackMethod = "fallbackGlobalService")
	@Retry(name = "globalService")
	public ResponseEntity<Object> allProducts() {
		
		ResponseEntity<Object> responseEntity = null;
		String url = Constantes.URL_PRODUCTS;
		responseEntity  = restTemplate.getForEntity(url, Object.class); 		
		return responseEntity;
	}	
	
	public ResponseEntity<Object> fallbackGlobalService(Long id, Throwable t) {
	    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	            .body(Utilidades.respuestasError(Utilidades.codigoError,
	                    "service temporarily unavailable. Please try again later."));
	}


}
