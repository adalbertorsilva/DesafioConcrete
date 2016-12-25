package br.com.concrete.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import br.com.concrete.domain.Phone;
import br.com.concrete.domain.User;
import br.com.concrete.exception.ErrorResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test @Transactional
	public void shouldPersistAnUser(){
		
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<User> responseEntity = testRestTemplate.postForEntity("/signup", user, User.class);
		User createdUser = responseEntity.getBody();
		
		assertNotNull(createdUser.getId());
		assertNotNull(createdUser.getPhones().stream().findFirst().get().getId());
	}
	
	@Test @Transactional
	public void aPersistedUserMustHaveACreatedData(){
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao2@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<User> responseEntity = testRestTemplate.postForEntity("/signup", user, User.class);
		User createdUser = responseEntity.getBody();
		
		assertNotNull(createdUser.getCreated());
	}
	
	@Test @Transactional
	public void aPersistedUserMustHaveACreatedToken(){
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao3@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<User> responseEntity = testRestTemplate.postForEntity("/signup", user, User.class);
		User createdUser = responseEntity.getBody();
		
		assertNotNull(createdUser.getToken());
	}
	@Test @Transactional
	public void mustNotHaveADuplicatedUser(){
		
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao4@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		testRestTemplate.postForEntity("/signup", user, User.class);
		
		user = new User();
		user.setName("João da Silva");
		user.setEmail("joao4@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<ErrorResponse> response = testRestTemplate.postForEntity("/signup", user, ErrorResponse.class);
		
		assertTrue(response.getStatusCode().equals(HttpStatus.PRECONDITION_FAILED));
		assertNotNull(response.getBody().getMessage());
		assertFalse(response.getBody().getMessage().isEmpty());
		assertTrue(response.getBody().getMessage().equalsIgnoreCase("E-mail já existente"));
		
	}
	
	@Test @Transactional
	public void aPersistedUserMustHaveALastLoginDate(){
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao5@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<User> responseEntity = testRestTemplate.postForEntity("/signup", user, User.class);
		User createdUser = responseEntity.getBody();
		
		assertNotNull(createdUser.getLastLogin());
	}
	
	@Test @Transactional
	public void aPersistedUserMustHaveAModifiedDate(){
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao5@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<User> responseEntity = testRestTemplate.postForEntity("/signup", user, User.class);
		User createdUser = responseEntity.getBody();
		
		assertNotNull(createdUser.getModified());
	}
	
	@Test @Transactional
	public void userMustHaveAName(){
		
		User user = new User();
		user.setEmail("joao5@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<ErrorResponse> responseEntity = testRestTemplate.postForEntity("/signup", user, ErrorResponse.class);
		
		assertNotNull(responseEntity.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY));
	}

}
