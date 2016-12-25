package br.com.concrete.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
	
	private String message;
	
	public ErrorResponse() {}
	
	public ErrorResponse(String message) {
		super();
		this.message = message;
	}
	
	@JsonProperty("mensagem")
	public String getMessage() {
		return message;
	}	
}
