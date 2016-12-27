package br.com.concrete.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7604682472984750747L;

	public HttpStatus getHttpStatus(){
		return HttpStatus.UNAUTHORIZED;
	}
	
	public String getMessage() {
		return "Não autorizado";	
	}
}
