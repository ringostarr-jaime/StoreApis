package com.store;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.dto.order.*;
import com.store.dto.payment.PaymentDto;
import com.store.entity.client.Client;
import com.store.interfaces.order.OrderInterfaces;
import com.store.interfaces.payment.PaymentInterfaces;
import com.store.services.ClientService;

@SpringBootTest(classes = StoreApisApplication.class) 
@AutoConfigureMockMvc(addFilters = false)
class StoreApisApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderInterfaces orderService;
    private OrderDto validOrderDto;
    private DeleteOrderDto validDeleteOrderDto;    
    @MockBean
    private PaymentInterfaces payService;
    private PaymentDto validPaymentDto;    
    @MockBean
    private ClientService clientService;
    private com.store.dto.client.ClientDto validClientDto;
    private Client clientEntity;

    @BeforeEach
    void setUp() {
        ProductOrderDto product = new ProductOrderDto();
        product.setProductId(1L);
        product.setQuantity(2);
        product.setUnitPrice(new BigDecimal("50.00"));
        product.setProductName("Laptop");

        validOrderDto = new OrderDto();
        validOrderDto.setUserId(1L);
        validOrderDto.setShipingAdress("Av. Siempre Viva 123");
        validOrderDto.setProducts(List.of(product));

        DeleteProductDto deleteProduct = new DeleteProductDto();
        deleteProduct.setOrderDetailId(1L);
        deleteProduct.setProductId(1L);
        deleteProduct.setQuantity(1);

        validDeleteOrderDto = new DeleteOrderDto();
        validDeleteOrderDto.setUserId(1L);
        validDeleteOrderDto.setOrderDetailIds(List.of(deleteProduct));
        
        validPaymentDto = new PaymentDto();
        validPaymentDto.setUserId(1L);
        validPaymentDto.setAmount(new BigDecimal("100.00"));
        validPaymentDto.setIdOrder(10L);
        validPaymentDto.setIdTransaction(UUID.randomUUID());
        validPaymentDto.setPaymentType("CREDIT_CARD");
        validPaymentDto.setPaymentStatus("PENDING");
        
        validClientDto = new com.store.dto.client.ClientDto();
        validClientDto.setFirstName("Jaime");
        validClientDto.setLastName("Rodas");
        validClientDto.setEmail("jaime@example.com");
        validClientDto.setPhoneNumber("123456789");
        validClientDto.setAddress("Av. Siempre Viva 123");
        validClientDto.setCountry("El Salvador");
        validClientDto.setCity("San Salvador");
        validClientDto.setState("San Salvador");
        validClientDto.setUsername("jrodas");
        validClientDto.setPassword("securePassword123");

        clientEntity = new Client();
        clientEntity.setId(1L);
        clientEntity.setFirstName("Jaime");
        clientEntity.setLastName("Rodas");
        clientEntity.setEmail("jaime@example.com");
        clientEntity.setUsername("jrodas");
        clientEntity.setPassword("securePassword123");
    }

    @Test
    @DisplayName("addProductCart - OK")
    void addProductCart_ok() throws Exception {
        when(orderService.addProductCart(any(OrderDto.class)))
                .thenReturn(ResponseEntity.ok("OK"));

        mockMvc.perform(post("/v1/orders/addProductCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getOrder_ok() throws Exception {
        when(orderService.getOrder(anyString()))
                .thenReturn(ResponseEntity.ok("OK"));

        mockMvc.perform(get("/v1/orders/getOrder")
                        .param("username", "jrodas"))
                .andExpect(status().isOk());
    }

    @Test
    void adjustOrderDetails_ok() throws Exception {
        when(orderService.adjustOrderDetails(any(DeleteOrderDto.class)))
                .thenReturn(ResponseEntity.ok("OK"));

        mockMvc.perform(put("/v1/orders/adjustOrderDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDeleteOrderDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrder_ok() throws Exception {
        when(orderService.deleteOrder(anyLong()))
                .thenReturn(ResponseEntity.ok("OK"));

        mockMvc.perform(delete("/v1/orders/deleteOrder/1"))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("createPayment - OK")
    void createPayment_ok() throws Exception {
        when(payService.createPayment(any(PaymentDto.class)))
                .thenReturn(ResponseEntity.ok("Pago creado correctamente"));

        mockMvc.perform(post("/v1/payment/createPayment") 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPaymentDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getStatusPay - OK")
    void getStatusPay_ok() throws Exception {
        when(payService.findPayment(anyString(), anyLong()))
                .thenReturn(ResponseEntity.ok("Estado de pago encontrado"));

        mockMvc.perform(get("/v1/payment/getStatusPay")
                        .param("codeTransaction", "550e8400-e29b-41d4-a716-446655440000")
                        .param("idProduct", "101"))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("addClient - OK")
    void addClient_ok() throws Exception {
        when(clientService.saveClient(any(com.store.dto.client.ClientDto.class)))
                .thenReturn(clientEntity);

        mockMvc.perform(post("/v1/clients/addClient") 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validClientDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getClient - OK")
    void getClient_ok() throws Exception {
        when(clientService.findClientByUserName(anyString()))
                .thenReturn(ResponseEntity.ok(clientEntity));

        mockMvc.perform(get("/v1/clients/getClient")
                        .param("username", "jrodas"))
                .andExpect(status().isOk());
    }
    
}