package br.com.concrete.exception;

public class MissingFieldException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6786021606464933069L;

	@Override 
	public String getMessage() {
		return "Campo Obrigatório!";
	}

}
