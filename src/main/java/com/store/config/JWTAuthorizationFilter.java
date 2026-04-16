package com.store.config;



import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.store.utilidades.Utilidades;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";	
	@Value("${token.secretKey}") 
	private String SECRET;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		      
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request,100000);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        logger.info("***********");
        logger.info("==  Metodo HTTP: " + wrappedRequest.getMethod());
	    logger.info("==  URI: " + wrappedRequest.getRequestURI());
	    //logger.info("==  Authorization header: " + wrappedRequest.getHeader(HEADER));		
        logger.info("== RemoteAddr: "+ wrappedRequest.getRemoteAddr());  
		try {			
			if (existeJWTToken(wrappedRequest, wrappedResponse)) {
				Claims claims = validateToken(wrappedRequest);
				//System.out.println("Claims: " + claims);
				if (claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			} else {
					SecurityContextHolder.clearContext();
			}
			chain.doFilter(wrappedRequest, wrappedResponse);			
			
			//---requet
	        byte[] requestBytes = wrappedRequest.getContentAsByteArray();
	        String body = new String(requestBytes, wrappedRequest.getCharacterEncoding() != null 
	                ? wrappedRequest.getCharacterEncoding() : "UTF-8");
	        logger.info("== Header: " + wrappedRequest.getHeader(HEADER));
	        String sanitizedBody = Utilidades.sanitizeBody(body); 
	        logger.info("== Body: " + sanitizedBody);
	        
	        //---Response
	        byte[] responseBytes = wrappedResponse.getContentAsByteArray();
	        String responseBody = new String(responseBytes, wrappedResponse.getCharacterEncoding() != null
	                ? wrappedResponse.getCharacterEncoding() : "UTF-8");
	        logger.info("== Status Response: " + wrappedResponse.getStatus());
	        logger.info("== Body Response: " + Utilidades.sanitizeBody(responseBody));
	        logger.info("***********");
	        wrappedResponse.copyBodyToResponse();
			
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			wrappedResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) wrappedResponse).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}	

	 public Claims validateToken(HttpServletRequest request) {
	        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");

	        // Decodificar la clave secreta desde Base64
	        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
	        if (keyBytes.length < 64) {
	            throw new IllegalArgumentException("La clave debe ser de al menos 512 bits");
	        }

	        return Jwts.parserBuilder()
	                .setSigningKey(keyBytes)
	                .build()
	                .parseClaimsJws(jwtToken)
	                .getBody();
	    }

	/**
	 * Metodo para autenticarnos dentro del flujo de Spring
	 * 
	 * @param claims
	 */
	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	private boolean existeJWTToken(HttpServletRequest request, HttpServletResponse res) {
		String authenticationHeader = request.getHeader(HEADER);
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
			return false;
		return true;
	}

}

