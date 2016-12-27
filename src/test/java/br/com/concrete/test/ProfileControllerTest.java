package br.com.concrete.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test @Transactional
	public void mustHaveATokenAsHeaderParameterToRetreiveProfile(){
		
		HttpHeaders header = new HttpHeaders();
		header.add("token", null);
		
		HttpEntity requestEntity = new HttpEntity<>(header);
		
		ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("/profile/1", HttpMethod.GET, requestEntity, ErrorResponse.class);
		
		assertTrue(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
		
	}
	
	@Test @Transactional
	public void mustHaveTheSameTokenOfRetrievedUserToRetreiveProfile(){
		
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		ResponseEntity<User> responseUser = testRestTemplate.postForEntity("/signup", user, User.class);
		
		HttpHeaders header = new HttpHeaders();
		header.add("token", "some other token");
		
		HttpEntity requestEntity = new HttpEntity<>(header);
		
		ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("/profile/" + responseUser.getBody().getId(), HttpMethod.GET, requestEntity, ErrorResponse.class);
		
		assertTrue(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
		
	}
	
	@Test
	public void mustNotHaveLoggedInForThirtyMinutesToRetrieveProfile(){
		
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao2@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		testRestTemplate.postForEntity("/signup", user, User.class);
		ResponseEntity<User> responseUser =  testRestTemplate.postForEntity("/login", new Login(user.getEmail(), user.getPassword()), User.class);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(responseUser.getBody().getLastLogin());
		calendar.add(Calendar.MINUTE, -31);
		responseUser.getBody().setLastLogin(calendar.getTime());
		
		userRepository.save(responseUser.getBody());
		
		HttpHeaders header = new HttpHeaders();
		header.add("token", responseUser.getBody().getToken());
		HttpEntity requestEntity = new HttpEntity<>(header);
		
		ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("/profile/" + responseUser.getBody().getId(), HttpMethod.GET, requestEntity, ErrorResponse.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.FORBIDDEN));
	}
	
	@Test @Transactional
	public void mustRetrieveProfile(){
		
		User user = new User();
		user.setName("João da Silva");
		user.setEmail("joao2@silva.org");
		user.setPassword("hunter2");
		user.addPhone(new Phone(987654321, 21));
		
		testRestTemplate.postForEntity("/signup", user, User.class);
		ResponseEntity<User> responseUser =  testRestTemplate.postForEntity("/login", new Login(user.getEmail(), user.getPassword()), User.class);
		
		HttpHeaders header = new HttpHeaders();
		header.add("token", responseUser.getBody().getToken());
		HttpEntity requestEntity = new HttpEntity<>(header);
		
		ResponseEntity<User> response = testRestTemplate.exchange("/profile/" + responseUser.getBody().getId(), HttpMethod.GET, requestEntity, User.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}

}
