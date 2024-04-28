package com.trial.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import com.trial.config.JwtGeneratorValidator;
import com.trial.service.AuthenticationService;

@ExtendWith(MockitoExtension.class)
public class BaseControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtGeneratorValidator jwtValidator;
    
    @InjectMocks
    private BaseController baseController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject mock values
        ReflectionTestUtils.setField(baseController, "SECRET", "testSecret");
    }
    
    @Test
    @DisplayName("Test goToIndexPage method")
    public void testGoToIndexPage() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        // Act
        ModelAndView mv = baseController.goToIndexPage(request, response);

        // Assert
        assertEquals("index", mv.getViewName());
    }
    
    @Test
    @DisplayName("Test goToHiddenPage method")
    public void testGoToHiddenPage() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        // Act
        ModelAndView mv = baseController.goToHiddenSuperAdminLoginForm(request, response);

        // Assert
        assertEquals("hidden", mv.getViewName());
    }
    
    @Test
    @DisplayName("Test logout method")
    public void testLogout() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);       

        // Act
        ModelAndView modelAndView = baseController.logout(request, response, redirectAttributes);

        // Assert
        assertEquals("redirect:", modelAndView.getViewName());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
    
    @Test
    @DisplayName("Test authenticationSuperAdmin method with valid credentials")
    public void testAuthenticationSuperAdminWithValidCredentials() throws Exception {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock RedirectAttributes
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        // Mock authentication result
        when(authenticationService.authenticateSuperAdmin(anyString())).thenReturn(true);

        // Mock authentication manager behavior
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Mock superAdminKey
        String mockSuperAdminKey = "018ca101-8bd5-7d8f-981f-5dd258c5d917";

        // Act
        ModelAndView modelAndView = baseController.authenticationSuperAdmin(mockSuperAdminKey, request, response, redirectAttributes);

        // Assert
        assertEquals("redirect:/api/v1/superadmins/home", modelAndView.getViewName());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Test genericAuthentication method - User")
    public void testGenericAuthenticationFormWithValidUserCredentials() {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        // Mock RedirectAttributes
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        // Mock authentication result
        when(authenticationService.authenticateUser(anyString(), anyString())).thenReturn(true);
        when(authenticationService.authenticateManager(anyString(), anyString())).thenReturn(false);

        // Mock authentication manager behavior
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("UserTest1", "NewPassword@1", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Mock JWT tokens
        String mockToken = "mockToken";
        String mockRefreshToken = "mockRefreshToken";
        when(jwtValidator.generateToken(any())).thenReturn(mockToken);
        when(jwtValidator.generateRefreshToken(any())).thenReturn(mockRefreshToken);

        // Mock form parameters
        String username = "UserTest1";
        String password = "NewPassword@1";

        // Act
        ModelAndView modelAndView = baseController.genericAuthenticationForm(username, password, request, response, redirectAttributes);

        // Assert
        assertEquals("redirect:/api/v1/users/home", modelAndView.getViewName());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
    
    @Test
    @DisplayName("Test genericAuthentication method - Manager")
    public void testGenericAuthenticationFormWithValidManagerCredentials() {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        // Mock RedirectAttributes
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();

        // Mock authentication result
        when(authenticationService.authenticateUser(anyString(), anyString())).thenReturn(false);
        when(authenticationService.authenticateManager(anyString(), anyString())).thenReturn(true);

        // Mock authentication manager behavior
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("ManagerTest1", "NewPassword@1", Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Mock JWT tokens
        String mockToken = "mockToken";
        String mockRefreshToken = "mockRefreshToken";
        when(jwtValidator.generateToken(any())).thenReturn(mockToken);
        when(jwtValidator.generateRefreshToken(any())).thenReturn(mockRefreshToken);

        // Mock form parameters
        String username = "ManagerTest1";
        String password = "NewPassword@1";

        // Act
        ModelAndView modelAndView = baseController.genericAuthenticationForm(username, password, request, response, redirectAttributes);

        // Assert
        assertEquals("redirect:/api/v1/managers/home", modelAndView.getViewName());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
