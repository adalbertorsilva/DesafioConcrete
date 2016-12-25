package br.com.concrete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.concrete.domain.User;
import br.com.concrete.exception.DuplicatedEmailException;
import br.com.concrete.exception.ErrorResponse;
import br.com.concrete.service.UserService;

@RestController
@RequestMapping("/signup")
public class SignUpController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<User> signUp(@RequestBody User requestUser) throws DuplicatedEmailException{
		return new ResponseEntity<User>(userService.createUser(requestUser), HttpStatus.OK);
	}
	
	@ExceptionHandler(DuplicatedEmailException.class)
	public ResponseEntity<ErrorResponse> duplicatedEmailExceptionHandler(DuplicatedEmailException ex){
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage()), ex.getHttpStatus());
	}
	
	
}
