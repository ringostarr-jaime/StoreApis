package com.store.utilidades;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Utilidades {
	
	public static final String codigoOK= "0000";
	public static final String codigoError= "2000";
	
	public static BigDecimal formato(int quantity, BigDecimal unitPrice) {
	    BigDecimal cantidad = BigDecimal.valueOf(quantity);
	    BigDecimal total = unitPrice.multiply(cantidad);
	    return total;
	}

	
	public static String sanitizeBody(String body) {
	    if (body == null || body.isEmpty()) return body;
	    
	    // Reemplaza por ****
	    return body
	        .replaceAll("(\"password\"\\s*:\\s*\")([^\"]*)(\")", "$1****$3")
	        .replaceAll("(\"token\"\\s*:\\s*\")([^\"]*)(\")", "$1****$3")
	        .replaceAll("(\"secret\"\\s*:\\s*\")([^\"]*)(\")", "$1****$3")
	        .replaceAll("(\"creditCard\"\\s*:\\s*\")([^\"]*)(\")", "$1****$3");
	}
	
	
	public static Map<String, String> respuestasError(String codigo,String mensaje){
	 Map<String, String> errors = new HashMap<>();
	 errors.put("codigo", codigo);
	 errors.put("error", mensaje);
	 return errors;
	}
	
	public static Map<String, Object> respuestasOk(String codigo, Object mensaje) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("codigo", codigo);
	    response.put("body", mensaje);
	    return response;
	}

}

