package com.trial.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;
import com.trial.config.JwtGeneratorValidator;
import com.trial.constants.Role;
import com.trial.pojo.Manager;
import com.trial.service.ManagerService;

@ExtendWith(MockitoExtension.class)
public class ManagerControllerTest {

    @Mock
    private JwtGeneratorValidator jwtValidator;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    @Test
    @DisplayName("Test goToManagerHomePage method")
    public void testGoToManagerHomePage() {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock the token creation
        String token = "mockedToken";

        // Mock the return of token from the cookie
        Cookie[] cookies = new Cookie[]{new Cookie("token", token)};
        when(request.getCookies()).thenReturn(cookies);

        // Mock username extraction
        when(jwtValidator.extractUsername(token)).thenReturn("testmanager");

        // Mock manager data retrieval
        Manager manager = new Manager();
        manager.setManagerId(new ObjectId("123456789012345678901234"));
        manager.setName("Test Manager");
        manager.setRole(Role.MANAGER);
        when(managerService.findManagerByUsername("testmanager")).thenReturn(Optional.of(manager));

        // Act
        ModelAndView modelAndView = managerController.goToManagerHomePage(request, response);

        // Assert
        assertEquals("managerHome", modelAndView.getViewName());
        assertEquals("Test Manager", modelAndView.getModel().get("name"));
        assertEquals("Manager Role", ((Role) modelAndView.getModel().get("role")).getCustomName());
    }
}
