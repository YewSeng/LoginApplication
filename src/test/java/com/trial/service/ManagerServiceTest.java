package com.trial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.trial.pojo.Manager;
import com.trial.repository.ManagerRepository;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private BcryptService bcryptService;

    @InjectMocks
    private ManagerService managerService;
    
    @Test
    @DisplayName("Test findManagerById method")
    public void testFindManagerById() {
    	// Arrange 
        ObjectId managerId = new ObjectId();
        Manager manager = new Manager("John", "john_doe", "password");
        manager.setManagerId(managerId);
        
        // Act
        when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
        Optional<Manager> foundManagerOptional = managerService.findManagerById(managerId);
        
        // Assert
        assertTrue(foundManagerOptional.isPresent());
        assertEquals(manager, foundManagerOptional.get());
    }

    @Test
    @DisplayName("Test findManagerByUsername method")
    public void testFindManagerByUsername() {
    	// Arrange
        String username = "username";
        Manager manager = new Manager("John", "username", "password");
        
        // Act
        when(managerRepository.findByUsername(username)).thenReturn(Optional.of(manager));
        Optional<Manager> foundManagerOptional = managerService.findManagerByUsername(username);
        
        // Assert
        assertTrue(foundManagerOptional.isPresent());
        assertEquals(manager, foundManagerOptional.get());
    }
    
    @Test
    @DisplayName("Test createManager method")
    public void testCreateManager() {
    	// Arrange
    	Manager manager = new Manager("John", "john_doe", "password");
        Manager hashedManager = new Manager(manager.getName(), manager.getUsername(), bcryptService.hashPassword(manager.getPassword()));

        // Act
        when(managerRepository.save(Mockito.any(Manager.class))).thenReturn(hashedManager);
        Manager createdManager = managerService.createManager(manager);
        
        // Assert
        assertEquals(hashedManager.getPassword(), createdManager.getPassword());
    }
    
    @Test
    @DisplayName("Test verifyExistingUsername method")
    public void testVerifyExistingUsername() {
    	// Arrange
        String username = "john doe";
        
        // Act
        when(managerRepository.findByUsername(username)).thenReturn(Optional.of(new Manager()));

        // Assert
        assertTrue(managerService.verifyExistingUsername(username));
    }
}
