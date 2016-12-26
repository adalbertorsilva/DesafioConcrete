package br.com.concrete.exception;

import org.springframework.http.HttpStatus;

public class MissingFieldException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6786021606464933069L;

	@Override 
	public String getMessage() {
		return "Campo Obrigatório!";
	}
	
	public HttpStatus getHttpStatus(){
		return HttpStatus.UNPROCESSABLE_ENTITY;
	}

}
