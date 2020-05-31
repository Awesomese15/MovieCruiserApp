package com.stackroute.moviecruiser.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when; 
import static org.mockito.Mockito.verify;

import com.stackroute.moviecruiser.exceptions.UserAlreadyExistsException;
import com.stackroute.moviecruiser.exceptions.UserNotFoundException;
import com.stackroute.moviecruiser.model.User;
import com.stackroute.moviecruiser.repository.UserRepository;



public class UserServiceTest {

	@Mock
	UserRepository userRepository;
	
	private User user;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	Optional<User> options;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		user = new User("Aneesh","Aneesh","Kapur","123456",new Date());
		options = Optional.of(user);
	}
	
	@Test
	public void testSaveUserSuccess() throws UserAlreadyExistsException, UserNotFoundException
	{
		when(userRepository.save(user)).thenReturn(user);
		boolean flag = userServiceImpl.saveUser(user);
		assertEquals("Cannot register User",true,flag);
		verify(userRepository, times(1)).save(user);
	}
	
	@Test(expected = UserAlreadyExistsException.class)
	public void testSaveUserFailure() throws UserAlreadyExistsException, UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(options);
		when(userRepository.save(user)).thenReturn(user);
		boolean flag = userServiceImpl.saveUser(user);
	
	}
	
	@Test
	public void testValidateSuccess() throws UserNotFoundException
	{
		when(userRepository.findByUserIdAndPassword(user.getUserId(), user.getPassword())).thenReturn(user);
		User userResult = userServiceImpl.findByUserIdAndPassword(user.getUserId(), user.getPassword());
		assertNotNull(userResult);
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testValidateFailure() throws UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(null);
		 userServiceImpl.findByUserIdAndPassword(user.getUserId(), user.getPassword());
		
	}

}
