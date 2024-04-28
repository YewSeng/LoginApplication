package com.trial.service;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trial.constants.Role;
import com.trial.pojo.Manager;
import com.trial.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagerService {

	private ManagerRepository managerRepository;
	private BcryptService bcryptService;
	
	@Autowired
	public ManagerService(ManagerRepository managerRepository, BcryptService bcryptService) {
		this.managerRepository = managerRepository;
		this.bcryptService = bcryptService;
	}
	
	public Manager createManager(Manager manager) {
    	log.info("Creating a new Manager with username: {}", manager.getUsername());
    	manager.setRole(Role.MANAGER);
    	manager.setPassword(bcryptService.hashPassword(manager.getPassword()));
        return managerRepository.save(manager);
	}
	
	public Optional<Manager> findManagerById(ObjectId managerId) {
		return managerRepository.findById(managerId);
	}
	
    public Optional<Manager> findManagerByUsername(String username) {
    	return managerRepository.findByUsername(username);
    }
    
    public Boolean verifyExistingUsername(String username) {
    	return managerRepository.findByUsername(username).isPresent();
    }
}
