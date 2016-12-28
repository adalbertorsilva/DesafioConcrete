package br.com.concrete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.concrete.domain.User;
import br.com.concrete.exception.ErrorResponse;
import br.com.concrete.exception.ExpiredTokenException;
import br.com.concrete.exception.UnauthorizedException;
import br.com.concrete.service.UserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserService userService;

	@RequestMapping(method=RequestMethod.GET, value="{id}")
	public ResponseEntity<User> getProfile(@RequestHeader("token") String token, @PathVariable Long id) throws Exception{
		ResponseEntity<User> responseUser = new ResponseEntity<User>(userService.getUserForProfile(token, id), HttpStatus.OK);
		return responseUser;
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> unauthorizedExceptionHandler(UnauthorizedException ex){
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getLocalizedMessage()), ex.getHttpStatus());
	}
	
	@ExceptionHandler(ExpiredTokenException.class)
	public ResponseEntity<ErrorResponse> expiredTokenExceptionHandler(ExpiredTokenException ex){
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getLocalizedMessage()), ex.getHttpStatus());
	}
	
}
