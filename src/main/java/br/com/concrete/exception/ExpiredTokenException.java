package br.com.concrete.exception;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7122492159478336641L;

	public HttpStatus getHttpStatus(){
		return HttpStatus.FORBIDDEN;
	}
	
	public String getMessage() {
		return "Sessão inválida";	
	}
	
}
