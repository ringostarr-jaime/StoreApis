package com.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.store.entity.payment.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
	 Payment findByCodeTransactionAndClientId(String codeTransaction,Long idClient);

}
