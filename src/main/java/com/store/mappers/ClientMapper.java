package com.store.mappers;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.store.dto.client.ClientDto;
import com.store.entity.client.Client;

@Component
public class ClientMapper {
	
	private final PasswordEncoder passwordEncoder;
   
    public ClientMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }	
	
    public  Client toEntity(ClientDto dto) {
        if (dto == null) {
            return null;
        }        
        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setAddress(dto.getAddress());
        client.setCountry(dto.getCountry());
        client.setCity(dto.getCity());
        client.setState(dto.getState());
        client.setUsername(dto.getUsername());    
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setModificationDate(LocalDateTime.now());
        return client;
    }

}

