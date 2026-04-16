package com.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.store.dto.client.ClientDto;
import com.store.entity.client.Client;
import com.store.services.ClientService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("${client.version}clients")
public class ClientController  {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/addClient")
    public Client saveClient(@Valid @RequestBody ClientDto client) {    
        return clientService.saveClient(client);
    }
    
    @GetMapping("/getClient")
	public ResponseEntity<Object> getClient(@RequestParam("username") String username)
	{ 
		return clientService.findClientByUserName(username);
	}
}

