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
import com.trial.pojo.User;
import com.trial.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BcryptService bcryptService;

    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Test findUserById method")
    public void testFindUserById() {
    	// Arrange
        ObjectId userId = new ObjectId();
        User user = new User("John", "john_doe", "password");
        user.setUserId(userId);
        
        // Act
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<User> foundUserOptional = userService.findUserById(userId);
        
        // Assert
        assertTrue(foundUserOptional.isPresent());
        assertEquals(user, foundUserOptional.get());
    }

    @Test
    @DisplayName("Test findUserByUsername method")
    public void testFindUserByUsername() {
    	// Arrange
        String username = "username";
        User user = new User("John", "username", "password");
        
        // Act
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Optional<User> foundUserOptional = userService.findUserByUsername(username);
        
        // Assert
        assertTrue(foundUserOptional.isPresent());
        assertEquals(user, foundUserOptional.get());
    }
    
    @Test
    @DisplayName("Test createUser method")
    public void testCreateUser() {
    	// Arrange
        User user = new User("John", "john_doe", "password");
        User hashedUser = new User(user.getName(), user.getUsername(), bcryptService.hashPassword(user.getPassword()));

        // Act
        when(userRepository.save(Mockito.any(User.class))).thenReturn(hashedUser);
        User createdUser = userService.createUser(user);
        
        // Assert
        assertEquals(hashedUser.getPassword(), createdUser.getPassword());
    }
    
    @Test
    @DisplayName("Test verifyExistingUsername method")
    public void testVerifyExistingUsername() {
    	// Arrange
        String username = "john doe";
        
        // Act
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        // Assert
        assertTrue(userService.verifyExistingUsername(username));
    }
}
