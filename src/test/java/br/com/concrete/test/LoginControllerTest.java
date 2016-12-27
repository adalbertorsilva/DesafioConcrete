package br.com.concrete.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.concrete.Application;
import br.com.concrete.domain.Login;
import br.com.concrete.domain.Phone;
import br.com.concrete.domain.User;
import br.com.concrete.exception.ErrorResponse;
import br.com.concrete.repository.UserRepository;
import br.com.concrete.utils.Utils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test @Transactional
	public void mustHaveaNonNullEmailToLogin(){
		
		ResponseEntity<ErrorResponse> response = testRestTemplate.postForEntity("/login", new Login(null, "hunter2"), ErrorResponse.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY));
	}
	
	@Test @Transactional
	public void mustHaveaNonNullPasswordToLogin(){
		
		ResponseEntity<ErrorResponse> response = testRestTemplate.postForEntity("/login", new Login("joao@silva.org", null), ErrorResponse.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY));
	}
	
	@Test @Transactional
	public void mustHaveAnExisitingEmailToLogin(){
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		testRestTemplate.postForEntity("/signup", user, User.class);
		ResponseEntity<ErrorResponse> response = testRestTemplate.postForEntity("/login", new Login("unknow@email.org", "hunter2"), ErrorResponse.class);
		
		assertTrue(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
	}
	
	@Test @Transactional
	public void mustHaveAMatchingPasswordToLogin(){
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao2@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		testRestTemplate.postForEntity("/signup", user, User.class);
		ResponseEntity<ErrorResponse> response = testRestTemplate.postForEntity("/login", new Login("joao2@silva.org", "nonMatchingPassword"), ErrorResponse.class);
		
		assertTrue(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
	}
	
	@Test @Transactional
	public void mustRetreiveAnUserByLogin(){
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao3@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		testRestTemplate.postForEntity("/signup", user, User.class);
		ResponseEntity<User> response = testRestTemplate.postForEntity("/login", new Login("joao3@silva.org", "hunter2"), User.class);
		
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		assertFalse(Utils.isNull(response.getBody()));
	}
	
	
	@Test @Transactional
	public void mustUpdateLastLoginWhenLogin() throws ParseException{
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao4@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		testRestTemplate.postForEntity("/signup", user, User.class);
		ResponseEntity<User> response = testRestTemplate.postForEntity("/login", new Login("joao4@silva.org", "hunter2"), User.class);
		
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		assertFalse(Utils.isNull(response.getBody()));
		assertNotNull(response.getBody().getLastLogin());
	}
	
}
