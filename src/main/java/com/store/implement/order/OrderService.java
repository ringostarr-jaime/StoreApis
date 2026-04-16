package com.store.implement.order;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.store.dto.order.ClientDto;
import com.store.dto.order.DeleteOrderDto;
import com.store.dto.order.DeleteProductDto;
import com.store.dto.order.OrderDto;
import com.store.dto.order.ProductOrderDto;
import com.store.entity.client.Client;
import com.store.entity.order.Order;
import com.store.entity.order.OrderDetail;
import com.store.interfaces.order.OrderInterfaces;
import com.store.repository.OrderDetailRepository;
import com.store.repository.OrderRepository;
import com.store.services.ClientService;
import com.store.utilidades.ErrorExceptionHandler;
import com.store.utilidades.Utilidades;

import jakarta.transaction.Transactional;


@Service
public class OrderService implements OrderInterfaces{		
	
	private final OrderRepository orderRepository;
	private final ClientService clientService;
	private final OrderDetailRepository detailRepository;

	public OrderService(OrderRepository orderRepository, ClientService clientService,
			OrderDetailRepository detailRepository) {
		super();
		this.orderRepository = orderRepository;
		this.clientService = clientService;
		this.detailRepository = detailRepository;
	}

	@Override
	public ResponseEntity<Object> findByUserIdAndStatus(Long id, String status) {
	    List<Order> orders = orderRepository.findByClient_IdAndOrderStatus(id, status);
	    if (orders.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(Utilidades.respuestasError(Utilidades.codigoError,"Client dont have any order"));
	    }
	    return ResponseEntity.ok(orders);
	}

	@Override
	public ResponseEntity<Object> addProductCart(OrderDto order) {
		List<OrderDto> listCart = new ArrayList<>();
		List<ProductOrderDto> listProduct = new ArrayList<>();
		ResponseEntity<Object> responseEntity = null;			
		OrderDto cart = new OrderDto();
		Long resp = 0L;
		try {			
			Order pendingOrder = orderRepository.findFirstByClient_IdAndOrderStatus(order.getUserId(), "PENDING");	
							
			BigDecimal totalAmount = BigDecimal.ZERO;
			listProduct = new ArrayList<>();
			listCart = new ArrayList<>();
			cart.setUserId(order.getUserId());	
			cart.setShipingAdress(order.getShipingAdress());
			ErrorExceptionHandler.logger.info("LIST FILLING addProductCart for client id "+order.getUserId());
			for (int i = 0; i < order.getProducts().size(); i++) {
				ProductOrderDto prOrder = new ProductOrderDto();
				prOrder.setProductId(order.getProducts().get(i).getProductId());
				prOrder.setQuantity(order.getProducts().get(i).getQuantity());			
				BigDecimal price = Utilidades.formato(order.getProducts().get(i).getQuantity(), order.getProducts().get(i).getUnitPrice());
				prOrder.setTotalPrice(price);
				prOrder.setUnitPrice(order.getProducts().get(i).getUnitPrice()); 
				prOrder.setProductName(order.getProducts().get(i).getProductName());
				totalAmount=totalAmount.add(price);
				listProduct.add(prOrder);				
			}		
			cart.setProducts(listProduct);
			cart.setTotalAmount(totalAmount);
			listCart.add(cart);						
			resp = saveProduct(order,pendingOrder);						
			if(resp==0)
			{
				listCart = new ArrayList<>();
				cart = new OrderDto();				
				listCart.add(cart);				
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError,"The shopping cart is not available to the user"));
			}
			ErrorExceptionHandler.logger.info("completed successfully addProductCart");
			return ResponseEntity.status(HttpStatus.OK).body(Utilidades.respuestasOk(Utilidades.codigoOK, listCart));
		} catch (Exception e) {
			ErrorExceptionHandler.logger.info("ERROR ON addProductCart {}",e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError,"The service is unable to respond at the moment"));
		}		
		
	}
	
	@Transactional
	public Long saveProduct(OrderDto orderDto, Order pendingOrder) {
	    try {
	        Order saveOrder;

	        if (pendingOrder != null) {
	            ErrorExceptionHandler.logger.info("Updating existing order for client id: {}", orderDto.getUserId());	         
	            pendingOrder.setShippingAddress(orderDto.getShipingAdress());
	            pendingOrder.setOrderDate(LocalDateTime.now());	         
	            pendingOrder.getOrderDetails().clear();

	            BigDecimal totalAmount = BigDecimal.ZERO;
	            for (ProductOrderDto p : orderDto.getProducts()) {
	                OrderDetail detail = new OrderDetail();
	                detail.setProductId(p.getProductId());
	                detail.setProductName(p.getProductName());
	                detail.setQuantity(p.getQuantity());
	                detail.setUnitPrice(p.getUnitPrice());
	                BigDecimal price = Utilidades.formato(p.getQuantity(), p.getUnitPrice());
	                detail.setTotalPrice(price);
	                totalAmount = totalAmount.add(price);
	                detail.setOrder(pendingOrder); 
	                pendingOrder.getOrderDetails().add(detail);
	            }

	            pendingOrder.setTotalAmount(totalAmount);
	            saveOrder = orderRepository.save(pendingOrder); 
	            return saveOrder.getId();
	        }

	       
	        ErrorExceptionHandler.logger.info("Creating new order for client id: {}", orderDto.getUserId());
	        Order order = new Order();
	        Client client = new Client();
	        client.setId(orderDto.getUserId());
	        order.setClient(client);
	        order.setShippingAddress(orderDto.getShipingAdress());
	        order.setOrderStatus("PENDING");
	        order.setOrderDate(LocalDateTime.now());

	        BigDecimal totalAmount = BigDecimal.ZERO;
	        List<OrderDetail> details = new ArrayList<>();
	        for (ProductOrderDto p : orderDto.getProducts()) {
	            OrderDetail detail = new OrderDetail();
	            detail.setProductId(p.getProductId());
	            detail.setProductName(p.getProductName());
	            detail.setQuantity(p.getQuantity());
	            detail.setUnitPrice(p.getUnitPrice());
	            BigDecimal price = Utilidades.formato(p.getQuantity(), p.getUnitPrice());
	            detail.setTotalPrice(price);
	            totalAmount = totalAmount.add(price);
	            detail.setOrder(order);
	            details.add(detail);
	        }

	        order.setTotalAmount(totalAmount);
	        order.setOrderDetails(details);
	        saveOrder = orderRepository.save(order); 
	        return saveOrder.getId();

	    } catch (Exception e) {
	        ErrorExceptionHandler.logger.error("ERROR ON saveProduct {}", e);
	        return 0L;
	    }
	}
	
	public List<Order> getOrderByStatus(Long id, String status)
	{
		return orderRepository.findByClient_IdAndOrderStatus(id, status);
	}

	@Override
	public ResponseEntity<Object> getOrder(String username) {		
	    try {
	    	Client client = clientService.findClient(username);
	        // 
	        List<Order> pendingOrder = getOrderByStatus(client.getId(), "PENDING");
	        ErrorExceptionHandler.logger.info("Resultado: {}", pendingOrder.size());
	        
	        if (pendingOrder.isEmpty()) {	            
	            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError,"There is no information to retrieve"));
	        }
	        
	        return ResponseEntity.status(HttpStatus.OK).body(Utilidades.respuestasOk(Utilidades.codigoOK, pendingOrder));

	    } catch (Exception e) {
	        ErrorExceptionHandler.logger.error("ERROR ON getOrder {}", e.getMessage(), e);	       
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError,"The service is unable to respond at the moment"));
	        
	    }
	}
	
	
	
	@Override
	@Transactional
	public ResponseEntity<Object> adjustOrderDetails(DeleteOrderDto orderDto) {
	    try {	      
	        Order clientOrder = orderRepository.findFirstByClient_IdAndOrderStatus(orderDto.getUserId(), "PENDING");

	        if (clientOrder == null) {	            
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Utilidades.respuestasError(Utilidades.codigoError,"There are no pending orders for customer"));
	        }

	        int totalRemoved = 0;	        
	        OrderDetail detail = null;
	        for (DeleteProductDto prodDto : orderDto.getOrderDetailIds()) {
	           
	        	for (OrderDetail d : clientOrder.getOrderDetails()) {
	        	    if (d.getProductId()== prodDto.getProductId() && d.getId()==prodDto.getOrderDetailId()) {
	        	        detail = d;
	        	        break; 
	        	    }
	        	}

	        	if (detail != null) {
	        	    if (prodDto.getQuantity() >= detail.getQuantity()) {
	        	        // Eliminar el detalle completo
	        	        clientOrder.getOrderDetails().remove(detail);
	        	        detailRepository.delete(detail);
	        	        totalRemoved += detail.getQuantity();
	        	    } else {
	        	        // Reducir la cantidad
	        	        int nuevaCantidad = detail.getQuantity() - prodDto.getQuantity();
	        	        detail.setQuantity(nuevaCantidad);
	        	        detailRepository.save(detail);
	        	        totalRemoved += prodDto.getQuantity();
	        	    }
	        	}

	        }

	        if (totalRemoved == 0) {	            
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Utilidades.respuestasError(Utilidades.codigoError,"No matching order details found to delete"));
	        }

	       
	        BigDecimal newTotal = BigDecimal.ZERO;
	        for (OrderDetail detail1 : clientOrder.getOrderDetails()) {
	            BigDecimal subtotal = Utilidades.formato(detail1.getQuantity(), detail1.getUnitPrice());
	            newTotal = newTotal.add(subtotal);
	        }
	        
	        if (clientOrder.getOrderDetails().isEmpty()) {
	            orderRepository.delete(clientOrder);
	        }else
	        {
	        	clientOrder.setTotalAmount(newTotal);
		        orderRepository.save(clientOrder);
	        }

	        return ResponseEntity.status(HttpStatus.OK)
	                .body(Utilidades.respuestasOk(Utilidades.codigoOK,"Total number of products removed: " + totalRemoved +", new order total: " + newTotal));

	    } catch (Exception e) {
	        ErrorExceptionHandler.logger.error("ERROR ON deleteProdOrder {}", e);
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError,"The service is unable to respond at the moment"));
	    }
	}

	@Override
	public ResponseEntity<Object> deleteOrder(Long idOrder) {		
		try {
			orderRepository.deleteById(idOrder);
			Optional<Order> order = orderRepository.findById(idOrder);
			if (order.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK)
		                .body(Utilidades.respuestasOk(Utilidades.codigoOK,"The order: " + idOrder +", was removedl: "));
			}
	
		}  catch (Exception e) {
	        ErrorExceptionHandler.logger.error("ERROR ON deleteOrder {}", e);
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError,"The service is unable to respond at the moment"));
	    }
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError,"There is no information to retrieve"));
		
	}

	

}

