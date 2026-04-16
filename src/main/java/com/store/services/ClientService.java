package com.store.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.store.dto.client.ClientDto;
import com.store.dto.client.LoginDto;
import com.store.entity.client.Client;
import com.store.mappers.ClientMapper;
import com.store.repository.ClientRepository;
import com.store.utilidades.ErrorExceptionHandler;
import com.store.utilidades.Utilidades;

@Service
public class ClientService {
		
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public Client saveClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        return clientRepository.save(client);
    }
    
    public Client findClient(String username)
    {
    	return clientRepository.findByUsername(username);		
    }
    
    public ResponseEntity<Object> findClientByUserName(String username)
    {
    	try {
			ResponseEntity<Object> responseEntity = null;	
			Client client = findClient(username);
			if(client!=null)					
			{	
				ErrorExceptionHandler.logger.info("Customer");
				//logger.info("Customer found");
				//return responseEntity.ok(client);	
				return ResponseEntity.status(HttpStatus.OK).body(Utilidades.respuestasOk(Utilidades.codigoOK, client));
			}			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Utilidades.respuestasError(Utilidades.codigoError, "Client not found"));		
		} catch (Exception e) {
			ErrorExceptionHandler.logger.info("ERROR ON getOrder {}",e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError, "The service is unable to respond at the moment"));
		}    	 
    	
    }
    
    public Client findByUserAndPassword(LoginDto loginDto)
    {
    	try {			
			Client client = clientRepository.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
			if(client!=null)					
			{
				ErrorExceptionHandler.logger.info("Customer found");
				return client;	
			}
			return null;		
		} catch (Exception e) {
			ErrorExceptionHandler.logger.info("ERROR ON getOrder {}",e);
			return null;
		}    	 
    	
    }
}

