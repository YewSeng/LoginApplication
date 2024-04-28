package com.trial.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.trial.constants.Role;
import com.trial.pojo.Manager;
import com.trial.pojo.User;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private AuthenticationService authenticationService;
    
    @Test
    @DisplayName("Test authenticateUser  method - ROLE_USER")
    public void testAuthenticateUserGivenUser() {
        // Arrange
    	User user = new User("name", "username", "password");
    	user.setRole(Role.USER);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act
        boolean isAuthenticated = authenticationService.authenticateUser(user.getUsername(), user.getPassword());

        // Assert
        assertTrue(isAuthenticated);
    }
    
    @Test
    @DisplayName("Test authenticateManager  method - ROLE_MANAGER")
    public void testAuthenticateManagerGivenManager() {
        // Arrange
    	Manager manager = new Manager("name", "username", "password");
    	manager.setRole(Role.MANAGER);
        Authentication authentication = new UsernamePasswordAuthenticationToken(manager, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act
        boolean isAuthenticated = authenticationService.authenticateManager(manager.getUsername(), manager.getPassword());

        // Assert
        assertTrue(isAuthenticated);
    }
    
    @Test
    @DisplayName("Test authenticateSuperAdmin  method - ROLE_SUPERADMIN")
    public void testAuthenticateSuperAdminGivenUser() {
        // Arrange
    	AuthenticationService authenticationService = new AuthenticationService(authenticationManager);    	
        String superAdminKey = "correctKey";
        authenticationService.setSuperAdminKey(superAdminKey);
        
        // Create UserDetails
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(superAdminKey, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUPERADMIN")));

        // Create UsernamePasswordAuthenticationToken with UserDetails
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        // Mock the authenticationManager behavior
        when(authenticationManager.authenticate(authenticationToken))
                .thenReturn(authenticationToken);
        // Act
        boolean isAuthenticated = authenticationService.authenticateSuperAdmin(superAdminKey);

        // Assert
        assertTrue(isAuthenticated);
    }
    
}
