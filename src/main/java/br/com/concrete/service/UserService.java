package br.com.concrete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.concrete.domain.Login;
import br.com.concrete.domain.User;
import br.com.concrete.exception.DuplicatedEmailException;
import br.com.concrete.exception.LoginException;
import br.com.concrete.exception.MissingFieldException;
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
		
		return user;
	}
	
}
