package com.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.store.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByClient_IdAndOrderStatus(Long clientId, String orderStatus);
	Order findFirstByClient_IdAndOrderStatus(Long clientId, String orderStatus);
	//@Query("SELECT o FROM Order o WHERE o.id IN :ids AND o.client.id = :clientId")
    //List<Order> findByIdsAndClientId(@Param("ids") List<Long> ids, @Param("clientId") Long clientId);
	List<Order> findByIdInAndClient_Id(List<Long> ids, Long clientId);
	
}

