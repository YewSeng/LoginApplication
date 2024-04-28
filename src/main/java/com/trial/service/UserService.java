package com.trial.service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trial.constants.Role;
import com.trial.pojo.User;
import com.trial.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	private UserRepository userRepository;
	private BcryptService bcryptService;
	
	@Autowired
	public UserService(UserRepository userRepository, BcryptService bcryptService) {
		super();
		this.userRepository = userRepository;
		this.bcryptService = bcryptService;
	}
	
    public User createUser(User user) {
    	log.info("Creating a new User with username: {}", user.getUsername());
    	user.setRole(Role.USER);
        user.setPassword(bcryptService.hashPassword(user.getPassword()));
        return userRepository.save(user);
    }
    
    public Optional<User> findUserById(ObjectId userId) {
    	return userRepository.findById(userId);
    }
    
    public Optional<User> findUserByUsername(String username) {
    	return userRepository.findByUsername(username);
    }
    
    public Boolean verifyExistingUsername(String username) {
    	return userRepository.findByUsername(username).isPresent();
    }
}
