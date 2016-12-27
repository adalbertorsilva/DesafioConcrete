package br.com.concrete.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.concrete.domain.Login;
import br.com.concrete.domain.User;
import br.com.concrete.exception.DuplicatedEmailException;
import br.com.concrete.exception.ExpiredTokenException;
import br.com.concrete.exception.LoginException;
import br.com.concrete.exception.MissingFieldException;
import br.com.concrete.exception.UnauthorizedException;
import br.com.concrete.repository.UserRepository;
import br.com.concrete.utils.Utils;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User createUser(User user) throws MissingFieldException, DuplicatedEmailException{
		validateFieldsEmptyness(user.getName(), user.getEmail(), user.getPassword());
		user.setSignUpAtributes();
		validateEmailUniquiness(user);
		return userRepository.save(user);
	}
	
	public User retrieveUser(Login login) throws MissingFieldException, LoginException{
		validateFieldsEmptyness(login.getEmail(), login.getPassword());
		return  getUserByLogin(login);
	}
	
	public User getUserForProfile(String token, Long id) throws UnauthorizedException, ExpiredTokenException {
		
		User user;
		validateTokenEmptyness(token);
		user = userRepository.findOne(id);
		validateUserToken(token, user);
		validateLastLogin(user);
		return null;
	}

	private void validateFieldsEmptyness(Object... fields) throws MissingFieldException{
		if(Utils.anyNull(fields) || 
				Utils.anyEmpty(fields)){
			throw new MissingFieldException();
		}
	}
	
	private void validateEmailUniquiness(User user) throws DuplicatedEmailException{
		if (!userRepository.findByEmail(user.getEmail()).isEmpty()){
			throw new DuplicatedEmailException();
		}
	}
	
	private User getUserByLogin(Login login) throws LoginException{
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword());
		
		if(Utils.isNull(user)){
			throw new LoginException();
		}
		
		user.setLastLogin(new Date());
		return userRepository.save(user);
	}
	
	private void validateTokenEmptyness(String token) throws UnauthorizedException {
		if(Utils.anyNull(token) || 
				Utils.anyEmpty(token)){
			throw new UnauthorizedException();
		}
	}
	
	private void validateUserToken(String token, User user) throws UnauthorizedException {
		if(!user.getToken().equals(token)){
			throw new UnauthorizedException();
		}
	}
	
	private void validateLastLogin(User user) throws ExpiredTokenException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime lastLoginTime = LocalDateTime.ofInstant(user.getLastLogin().toInstant(), ZoneId.systemDefault());
		
		if(lastLoginTime.until(now, ChronoUnit.MINUTES) > 30){
			throw new ExpiredTokenException();
		}
	}
}
