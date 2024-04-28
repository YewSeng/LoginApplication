package com.trial.controller;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.trial.config.JwtGeneratorValidator;
import com.trial.constants.Role;
import com.trial.pojo.User;
import com.trial.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtGeneratorValidator jwtValidator;
	
	@Value("${jwt.secretKey}")
    private String SECRET;
	
    private String extractTokenFromRequest(HttpServletRequest request) {
        // Get the cookies from the request
        Cookie[] cookies = request.getCookies();
        
        // Check if cookies exist and look for the token cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    // Found the token cookie, return its value
                    return cookie.getValue();
                }
            }
        }
        
        // Token cookie not found
        return null;
    }
    
    @GetMapping("/home")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView goToUserHomePage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Entered into the /home request");
        ModelAndView mv = new ModelAndView();
        
        mv.setViewName("userHome");
        log.info("Went to userHome.jsp page");
        
        // Extract the token from the request cookie
        String token = extractTokenFromRequest(request);
        log.info("Extracted token from request: {}", token);

        if (token != null) {
            // Parse the token to extract the username
            String username = jwtValidator.extractUsername(token);
            mv.addObject("username", username);
            Optional<User> userOptional = userService.findUserByUsername(username);
            if (userOptional.isPresent()) {
            	User user = userOptional.get();
            	ObjectId userId = user.getUserId();
            	String name = user.getName();
            	Role role = user.getRole();
            	System.out.println(role.getCustomName());
            	log.info("User Role: {}", role);
            	mv.addObject("userId", userId);
            	mv.addObject("name", name);
            	mv.addObject("role", role);
            }
        }

        // Set the response status to OK
        response.setStatus(HttpServletResponse.SC_OK);
        return mv;
    }
}
