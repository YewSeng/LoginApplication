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
import com.trial.pojo.User;
import com.trial.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private JwtGeneratorValidator jwtValidator;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Test goToUserHomePage method")
    public void testGoToUserHomePage() {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock the token creation
        String token = "mockedToken";

        // Mock the return of token from the cookie
        Cookie[] cookies = new Cookie[]{new Cookie("token", token)};
        when(request.getCookies()).thenReturn(cookies);

        // Mock username extraction
        when(jwtValidator.extractUsername(token)).thenReturn("testuser");

        // Mock user data retrieval
        User user = new User();
        user.setUserId(new ObjectId("123456789012345678901234"));
        user.setName("Test User");
        user.setRole(Role.USER);
        when(userService.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        ModelAndView modelAndView = userController.goToUserHomePage(request, response);

        // Assert
        assertEquals("userHome", modelAndView.getViewName());
        assertEquals("Test User", modelAndView.getModel().get("name"));
        assertEquals("User Role", ((Role) modelAndView.getModel().get("role")).getCustomName());
    }
}
