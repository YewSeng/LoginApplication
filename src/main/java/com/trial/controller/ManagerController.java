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
import com.trial.pojo.Manager;
import com.trial.service.ManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/v1/managers")
public class ManagerController {

	@Autowired
	private ManagerService managerService;
	
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
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ModelAndView goToManagerHomePage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Entered into the /home request");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("managerHome");
        log.info("Went to managerHome.jsp page");
        
        // Extract the token from the request cookie
        String token = extractTokenFromRequest(request);
        log.info("Extracted token from request: {}", token);

        if (token != null) {
            // Parse the token to extract the username
            String username = jwtValidator.extractUsername(token);
            mv.addObject("username", username);
            Optional<Manager> managerOptional = managerService.findManagerByUsername(username);
            if (managerOptional.isPresent()) {
            	Manager manager = managerOptional.get();
            	ObjectId managerId = manager.getManagerId();
            	String name = manager.getName();
            	Role role = manager.getRole();
            	mv.addObject("managerId", managerId);
            	mv.addObject("name", name);
            	mv.addObject("role", role);
            }
        }

        // Set the response status to OK
        response.setStatus(HttpServletResponse.SC_OK);
        return mv;
    }
}
