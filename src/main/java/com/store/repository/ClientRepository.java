package com.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.entity.client.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUsernameAndPassword(String userName, String password);
    Client findByUsername(String userName);
}
