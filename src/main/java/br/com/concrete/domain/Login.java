package br.com.concrete.domain;

public class Login {

	private String email;
	private String password;
	
	public Login() {}

	public Login(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
