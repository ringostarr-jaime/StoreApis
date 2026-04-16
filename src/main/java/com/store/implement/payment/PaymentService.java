package com.store.implement.payment;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.store.dto.payment.PaymentDto;
import com.store.entity.client.Client;
import com.store.entity.order.Order;
import com.store.entity.payment.Payment;
import com.store.interfaces.payment.PaymentInterfaces;
import com.store.repository.ClientRepository;
import com.store.repository.OrderRepository;
import com.store.repository.PaymentRepository;
import com.store.utilidades.Utilidades;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;



@Service
public class PaymentService implements PaymentInterfaces{
		
	private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          ClientRepository clientRepository,
                          OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @CircuitBreaker(name = "globalService", fallbackMethod = "fallbackGlobalService")
	@Retry(name = "globalService")
    public ResponseEntity<Object> createPayment(PaymentDto dto) {
        try {
            
            Optional<Client> clientTemp = clientRepository.findById(dto.getUserId());            
            if(clientTemp.isEmpty())
            {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Utilidades.respuestasError(Utilidades.codigoError,"Client not found"));	
            }            
            Client client = clientTemp.get();           
            
            Payment payStatus= paymentRepository.findByCodeTransactionAndClientId("PENDING",client.getId());            	
            if(payStatus!=null)
            {
            	 return ResponseEntity.status(HttpStatus.ACCEPTED)
                         .body(Utilidades.respuestasOk(Utilidades.codigoOK,"Your payment is being processed"));
            }
            
            payStatus= paymentRepository.findByCodeTransactionAndClientId("PAID",client.getId());            	
            if(payStatus!=null)
            {
            	 return ResponseEntity.status(HttpStatus.ACCEPTED)
                         .body(Utilidades.respuestasOk(Utilidades.codigoOK,payStatus));
            }            
            
            Optional<Order> orderTemp = orderRepository.findById(dto.getIdOrder());
            if(orderTemp.isEmpty())
            {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Utilidades.respuestasError(Utilidades.codigoError,"Order not found"));	
            }            
            Order order = orderTemp.get();        
            UUID uuid = UUID.randomUUID();            
         
            BigDecimal newTotal = order.getTotalAmount().subtract(dto.getAmount());           
            order.setTotalAmount(newTotal);            
            String status="PENDING";
            if (newTotal.compareTo(BigDecimal.ZERO) == 0||newTotal.compareTo(BigDecimal.ZERO) < 0) {
                order.setOrderStatus("PAID");
                status="PAID";
            }
            
            orderRepository.save(order);
            
            Payment payment = new Payment();
            payment.setAmount(dto.getAmount());
            payment.setClient(client);
            payment.setCodeTransaction(uuid.toString());
            payment.setOrder(order);           
            payment.setStatusPayment(status);            
            payment.setPaymentType(dto.getPaymentType());

            paymentRepository.save(payment);
            
            PaymentDto paymentdto = new PaymentDto();
            paymentdto.setUserId(client.getId());
            paymentdto.setIdOrder(order.getId());
            paymentdto.setIdTransaction(uuid);
            paymentdto.setAmount(dto.getAmount());
            paymentdto.setPaymentStatus(status);
            paymentdto.setPaymentType(dto.getPaymentType());
            paymentdto.setPaymentDate(LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Utilidades.respuestasOk(Utilidades.codigoError,paymentdto));

        } catch (Exception e) {         
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Utilidades.respuestasError(Utilidades.codigoError,"Error saving payment:"));
        }
    }

	@Override
	@CircuitBreaker(name = "globalService", fallbackMethod = "fallbackGlobalService")
	@Retry(name = "globalService")
	public ResponseEntity<Object> findPayment(String codeTransaction, Long idClient) {
		
		Payment payStatus= paymentRepository.findByCodeTransactionAndClientId(codeTransaction,idClient);
        if (payStatus!=null) {            
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Utilidades.respuestasOk(Utilidades.codigoOK,payStatus));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Utilidades.respuestasOk(Utilidades.codigoOK,"No payment found:"));
	}
	
	public ResponseEntity<Object> fallbackGlobalService(PaymentDto dto, Throwable t) {
	    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	            .body(Utilidades.respuestasError(Utilidades.codigoError,
	                    "service temporarily unavailable. Please try again later."));
	}
	
	public ResponseEntity<Object> fallbackGlobalService(String codeTransaction, Long idClient, Throwable t) {
	    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	            .body(Utilidades.respuestasError(Utilidades.codigoError,
	                    "service temporarily unavailable. Please try again later."));
	}
	
}

