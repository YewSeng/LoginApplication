package com.trial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.trial.pojo.Manager;
import com.trial.pojo.User;
import com.trial.repository.ManagerRepository;
import com.trial.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class DefaultUserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ManagerRepository managerRepository;
    
    @Mock
    private Environment environment; 
    
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    
    @InjectMocks
    private DefaultUserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        userService.setSuperAdminSecretKey("superSecretKey");
    }

    @Test
    @DisplayName("Test loadUserByUsername for SuperAdmin")
    public void testLoadUserByUsernameSuperAdmin() {
        // Arrange
        String superAdminSecretKey = "superSecretKey";
        
        // Act
        UserDetails userDetails = userService.loadUserByUsername(superAdminSecretKey);
        
        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof org.springframework.security.core.userdetails.User);
        assertEquals("Super Admin", userDetails.getUsername());
        assertEquals("ROLE_SUPERADMIN", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("Test loadUserByUsername for User")
    public void testLoadUserByUsernameUser() {
        // Arrange
        String username = "User";
        String password = "userpassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // Mocking User scenario
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().containsAll(
                AuthorityUtils.createAuthorityList("ROLE_USER")));
    }
    
    @Test
    @DisplayName("Test loadUserByUsername for Manager")
    public void testLoadUserByUsernameDoctor() {
        // Arrange
        String username = "Manager";
        String password = "doctorpassword";
        Manager manager = new Manager();
        manager.setUsername(username);
        manager.setPassword(password);

        // Mocking Manager scenario
        when(managerRepository.findByUsername(username))
                .thenReturn(Optional.of(manager));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().containsAll(
                AuthorityUtils.createAuthorityList("ROLE_MANAGER")));
    }
    
    @Test
    @DisplayName("Test loadUserByUsername throws UsernameNotFoundException")
    public void testLoadUserByUsernameUsernameNotFoundException() {
        // Arrange
        String username = "unknownUser";

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });
    }
}
