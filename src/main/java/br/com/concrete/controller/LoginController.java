package br.com.concrete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.concrete.domain.Login;
import br.com.concrete.domain.User;
import br.com.concrete.exception.ErrorResponse;
import br.com.concrete.exception.LoginException;
import br.com.concrete.exception.MissingFieldException;
import br.com.concrete.service.UserService;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<User> login(@RequestBody Login login) throws Exception{
		ResponseEntity<User> responseUser = new ResponseEntity<User>(userService.retrieveUser(login), HttpStatus.OK); 
		return responseUser;
	}
	
	@ExceptionHandler(MissingFieldException.class)
	public ResponseEntity<ErrorResponse> loginExceptionHandler(MissingFieldException ex){
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage()), ex.getHttpStatus());
	}
	
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<ErrorResponse> loginExceptionHandler(LoginException ex){
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage()), ex.getHttpStatus());
	}
	
}
