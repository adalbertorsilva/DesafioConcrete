package br.com.concrete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.concrete.domain.User;
import br.com.concrete.exception.DuplicatedEmailException;
import br.com.concrete.repository.UserRepository;

@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User createUser(User user) throws DuplicatedEmailException{
		user.setSignUpAtributes();
		validateEmailUniquiness(user);
		return userRepository.save(user);
	}
	
	private void validateEmailUniquiness(User user) throws DuplicatedEmailException{
		if (!userRepository.findByEmail(user.getEmail()).isEmpty()){
			throw new DuplicatedEmailException();
		}
	}
	
}
