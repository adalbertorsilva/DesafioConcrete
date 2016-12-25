package br.com.concrete.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5213873252021884724L;
	
	@Override
	public String getMessage() {
		return "E-mail já existente";
	}
	
	public HttpStatus getHttpStatus(){
		return HttpStatus.PRECONDITION_FAILED;
	}
}
