package br.com.concrete.exception;

import org.springframework.http.HttpStatus;

public class LoginException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1398952413150479468L;

	public HttpStatus getHttpStatus(){
		return HttpStatus.UNAUTHORIZED;
	}
	
	public String getMessage() {
		return "Usuário e/ou senha inválidos";	
	}
}
