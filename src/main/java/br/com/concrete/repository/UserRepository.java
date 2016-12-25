package br.com.concrete.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.concrete.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{
	public List<User> findByEmail(String email);
}
