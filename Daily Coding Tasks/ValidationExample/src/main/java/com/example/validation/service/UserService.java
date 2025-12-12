package com.example.validation.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.validation.dao.UserRepository;
import com.example.validation.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.validation.exception.ResourceNotFoundException;


@Service
public class UserService {
	private static final Logger Logger = LoggerFactory.getLogger(UserService.class);

	
	// Firstly autowired the UserRepository
	@Autowired
	public UserRepository userRepo;
	
	//UserRepository userRepo;
	
	public User saveUSer(User u)
	{
		return userRepo.save(u);
	}
	
	public List<User> getAllUsers()
	{
		return (List<User>) userRepo.findAll();
	}
	
	
	public Optional<User> getUserByIdNew(Long id) throws ResourceNotFoundException
	{
	    Logger.info("Fetching the user with id : {} " , id);

	    

	    if (!userRepo.existsById(id))
	    {
	        throw new ResourceNotFoundException("User Not Found with this id : " + id);
	    }

	    return userRepo.findById(id);  
	}

	
	public User updateUserById(Long id, User user)
	{
		User user1 = null;
		Optional<User> ouser = userRepo.findById(id);
		
		if(ouser.isPresent())
		{
			user1 = userRepo.save(user);
		}
		return user1;
	}
	
	public User partialUpdateUserById(Long id , User user)
	{
		User user1 = null;
		Optional<User> ouser = userRepo.findById(id);
		
		if(ouser.isPresent())
		{
			user1 = userRepo.save(user);
		}
		return user1;
	}
	
	public void deleteUserById(Long id) throws Exception
	{
		
		Optional<User> u = userRepo.findById(id);
		User user = u.orElseThrow(() -> new Exception("user with this id does not exist" + id));
	     userRepo.delete(user);
	}

}
