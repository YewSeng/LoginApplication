package com.trial.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.trial.config.JwtGeneratorValidator;
import com.trial.service.ManagerService;
import com.trial.service.UserService;

@ExtendWith(MockitoExtension.class)
public class SuperAdminControllerTest {


    @Mock
	private UserService userService;
    
    @Mock 
    private ManagerService managerService;
    
    @Mock
    private JwtGeneratorValidator jwtValidator;
    
    @InjectMocks
    private SuperAdminController superAdminController;
    
    @Test
    @DisplayName("Test goToSuperAdminHomePage method")
    public void testGoToSuperAdminHomePage() {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock the token creation
        String token = "mockedToken";

        // Mock the return of token from the cookie
        Cookie[] cookies = new Cookie[]{new Cookie("token", token)};
        when(request.getCookies()).thenReturn(cookies);

        // Mock username extraction
        when(jwtValidator.extractUsername(token)).thenReturn("Super Admin");

        // Act
        ModelAndView modelAndView = superAdminController.goToSuperAdminHomePage(request, response);

        // Assert
        assertEquals("superAdminHome", modelAndView.getViewName());
        assertEquals("Super Admin", modelAndView.getModel().get("username"));
    }
    
    @Test
    @DisplayName("Test goToCreateUserPage method - Success")
    public void testGoToCreateUserPage() {
        // Mock HttpServletRequest, HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        // Invoke the controller method
        ModelAndView mv = superAdminController.goToCreateUserPage(request, response);
        
        // Additional assertions
        assertEquals("createUser", mv.getViewName());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
    
    @Test
    @DisplayName("Test createUser method - Success")
    public void testCreateUserSuccess() throws Exception {
        // Mock HttpServletRequest, HttpServletResponse, RedirectAttributes
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Mock user data
        String name = "John Doe";
        String username = "UserTest1";
        String password = "Newpassword@1";

        // Mock UserService behavior
        when(userService.verifyExistingUsername(username)).thenReturn(false);

        // Invoke the controller method
        ModelAndView mv = superAdminController.createUser(name, username, password, request, response, redirectAttributes);

        // Verify behavior
        assertEquals("redirect:/api/v1/superadmins/createUser", mv.getViewName());
        verify(redirectAttributes).addFlashAttribute("successMessage", "User created successfully.");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
    
    @Test
    @DisplayName("Test goToCreateManagerPage method - Success")
    public void testGoToCreateManagerPage() {
        // Mock HttpServletRequest, HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        // Invoke the controller method
        ModelAndView mv = superAdminController.goToCreateManagerPage(request, response);
        
        // Additional assertions
        assertEquals("createManager", mv.getViewName());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
    
    @Test
    @DisplayName("Test createManager method - Success")
    public void testCreateManagerSuccess() throws Exception {
        // Mock HttpServletRequest, HttpServletResponse, RedirectAttributes
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Mock Manager data
        String name = "John Doe";
        String username = "ManagerTest1";
        String password = "Newpassword@1";

        // Mock ManagerService behavior
        when(managerService.verifyExistingUsername(username)).thenReturn(false);

        // Invoke the controller method
        ModelAndView mv = superAdminController.createManager(name, username, password, request, response, redirectAttributes);

        // Verify behavior
        assertEquals("redirect:/api/v1/superadmins/createManager", mv.getViewName());
        verify(redirectAttributes).addFlashAttribute("successMessage", "Manager created successfully.");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
     
}
