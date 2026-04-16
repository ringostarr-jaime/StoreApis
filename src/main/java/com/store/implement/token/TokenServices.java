package com.store.implement.token;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.dto.client.LoginDto;
import com.store.dto.token.User;
import com.store.entity.client.Client;
import com.store.interfaces.token.TokenInterfaces;
import com.store.services.ClientService;
import com.store.utilidades.Utilidades;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenServices implements TokenInterfaces {

	private final ClientService clientService;
	private final PasswordEncoder passwordEncoder;	
	
	public TokenServices(ClientService clientService,PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }	
	
	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);;	
	
	@Value("${token.secretKey}") 
	private String secretKey;

	@Override
	@CircuitBreaker(name = "globalService", fallbackMethod = "fallbackGlobalService")
	@Retry(name = "globalService")
	public ResponseEntity<Object> login(LoginDto loginDto) {		
				
		Client client = clientService.findClient(loginDto.getUsername());		
	    User user = new User();	    
	    try {
		    if (client == null || !passwordEncoder.matches(loginDto.getPassword(), client.getPassword())) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		                             .body(user); 	    
		        }
		    String token = getJWTToken(loginDto.getUsername());
		    
		    user.setUser(loginDto.getUsername());
		    user.setToken(token);
		} catch (Exception e) {			
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Utilidades.respuestasError(Utilidades.codigoError, "Faild generate token"));
		}
	    //return ResponseEntity.ok(user);
	    return ResponseEntity.status(HttpStatus.OK).body(Utilidades.respuestasOk(Utilidades.codigoOK, user));
	}

	@Override
	public String getJWTToken(String username) {
		byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("Error password");
        }
		
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("JWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) // 10 minutos
                .signWith(SignatureAlgorithm.HS512, keyBytes)
                .compact();

        return "Bearer " + token;
	}
	
	public ResponseEntity<Object> fallbackGlobalService(LoginDto loginDto, Throwable t) {
	    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	            .body(Utilidades.respuestasError(Utilidades.codigoError,
	                    "service temporarily unavailable. Please try again later."));
	}
	
}
