package com.trial.controller;

import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.trial.config.JwtGeneratorValidator;
import com.trial.exception.UsernameAlreadyExistException;
import com.trial.pojo.Manager;
import com.trial.pojo.User;
import com.trial.service.ManagerService;
import com.trial.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/v1/superadmins")
public class SuperAdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private JwtGeneratorValidator jwtValidator;
	
    @Value("${jwt.secretKey}")
    private String SECRET;
    
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty";
        } else if (name.trim().length() < 4) {
            return "Name must be at least 4 characters long";
        } else if (!name.matches("^[a-zA-Z ]{4,}$")) {
            return "Name must be at least 4 characters long and contain only letters";
        } else {
            return ""; // No error
        }
    }

    private String validateManagerUsername(String username) {
        if (!username.matches("^M[a-zA-Z0-9]{7,}$")) {
            return "Username must start with an uppercase 'M' followed by at least 7 characters";
        } else {
            return ""; // No error
        }
    }
    
    private String validateUserUsername(String username) {
        if (!username.matches("^U[a-zA-Z0-9]{7,}$")) {
            return "Username must start with an uppercase 'U' followed by at least 7 characters";
        } else {
            return ""; // No error
        }
    }

    private String validatePassword(String password) {
        if (!password.matches("^(?=.*[A-Z])(?=.*[@])(?=.*[0-9])[a-zA-Z0-9@]{8,}$")) {
            return "Password must be at least 8 characters long, contain at least 1 uppercase letter, 1 '@', and 1 number";
        } else {
            return ""; // No error
        }
    }
    
    private String validateUserForm(String name, String username, String password) {
        // Regular expressions for validation
    	String nameRegex = "^[a-zA-Z ]{4,}$";
        String usernameRegex = "^U[a-zA-Z0-9]{7,}$"; // Username must start with "U" (upper case) and followed by be at least 7 characters and contain only letters and numbers
        String passwordRegex = "^(?=.*[A-Z])(?=.*[@])(?=.*[0-9])[a-zA-Z0-9@]{8,}$"; // Password: At least 1 uppercase, 1 "@", 1 number, At least length 8

        // Validation checks
        StringBuilder errorMessage = new StringBuilder();

        if (name == null || name.trim().isEmpty()) {
            errorMessage.append("Name cannot be empty\n");
        } else if (name.trim().length() < 4) {
            errorMessage.append("Name must be at least 4 characters long\n");
        } else if (!name.matches(nameRegex)) {
            errorMessage.append("Name must contain only letters\n");
        }
        if (!username.matches(usernameRegex)) {
            errorMessage.append("Username must start with an uppercase 'U' followed by at least 7 characters\n");
        }
        if (!password.matches(passwordRegex)) {
            errorMessage.append("Password must be at least 8 characters long, contain at least 1 uppercase letter, 1 '@', and 1 number\n");
        }

        return errorMessage.toString();
    }
    
    private String validateManagerForm(String name, String username, String password) {
        // Regular expressions for validation
    	String nameRegex = "^[a-zA-Z ]{4,}$";
        String usernameRegex = "^M[a-zA-Z0-9]{7,}$"; // Username must start with "M" (upper case) and followed by be at least 7 characters and contain only letters and numbers
        String passwordRegex = "^(?=.*[A-Z])(?=.*[@])(?=.*[0-9])[a-zA-Z0-9@]{8,}$"; // Password: At least 1 uppercase, 1 "@", 1 number, At least length 8

        // Validation checks
        StringBuilder errorMessage = new StringBuilder();

        if (name == null || name.trim().isEmpty()) {
            errorMessage.append("Name cannot be empty\n");
        } else if (name.trim().length() < 4) {
            errorMessage.append("Name must be at least 4 characters long\n");
        } else if (!name.matches(nameRegex)) {
            errorMessage.append("Name must contain only letters\n");
        }
        if (!username.matches(usernameRegex)) {
            errorMessage.append("Username must start with an uppercase 'M' followed by at least 7 characters\n");
        }
        if (!password.matches(passwordRegex)) {
            errorMessage.append("Password must be at least 8 characters long, contain at least 1 uppercase letter, 1 '@', and 1 number\n");
        }

        return errorMessage.toString();
    }
    
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
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
    public ModelAndView goToSuperAdminHomePage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Entered into the /home request");
        ModelAndView mv = new ModelAndView();
                
        mv.setViewName("superAdminHome");
        log.info("Went to superAdminHome.jsp page");

        // Extract the token from the request cookie
        String token = extractTokenFromRequest(request);
        log.info("Extracted token from request: {}", token);

        if (token != null) {
            // Parse the token to extract the username
            String username = jwtValidator.extractUsername(token);
            mv.addObject("username", "Super Admin");
        }

        // Set the response status to OK
        response.setStatus(HttpServletResponse.SC_OK);
        return mv;
    }
    
	@GetMapping("/createUser")
	@PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
	public ModelAndView goToCreateUserPage(HttpServletRequest request, HttpServletResponse response) {
		log.info("Entered into the /createUser request");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("createUser");
		log.info("Went to createUser.jsp page");
	    // Get flash attributes and add them to the model
	    Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
	    if (flashMap != null) {
	        mv.addObject("name", flashMap.get("name"));
	        mv.addObject("username", flashMap.get("username"));
	        mv.addObject("password", flashMap.get("password"));
	    }
	    response.setStatus(HttpServletResponse.SC_OK);
		return mv;
	}
    
	@PostMapping("/registerUser")
	@PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
	public ModelAndView createUser(@RequestParam String name,
	                                @RequestParam String username,
	                                @RequestParam String password,
	                                HttpServletRequest request,
	                                HttpServletResponse response,
	                                RedirectAttributes redirectAttributes) {
	    log.info("Entered into the /registerUser request");
	    ModelAndView mv = new ModelAndView();
	    String nameError = "";
	    String usernameError = "";
	    String passwordError = "";
	    try {
	        // Perform form validation
	        nameError = validateName(name);
	        usernameError = validateUserUsername(username);
	        passwordError = validatePassword(password);

	        String errorMessage = validateUserForm(name, username, password);
	        if (!errorMessage.isEmpty()) {
	            throw new IllegalArgumentException(errorMessage);
	        }

	        if (!userService.verifyExistingUsername(username)) {
	            User user = new User();
	            user.setUserId(new ObjectId());
	            user.setName(name);
	            user.setUsername(username);
	            user.setPassword(password);

	            userService.createUser(user);

	            // Add success message attribute
	            redirectAttributes.addFlashAttribute("successMessage", "User created successfully.");
	            // Redirect to filtered view based on the username
	            mv.setViewName("redirect:/api/v1/superadmins/createUser");
	            response.setStatus(HttpServletResponse.SC_OK);
	            return mv;
	        } else {
	            log.error("Username is already taken. Please try again!");
	            throw new UsernameAlreadyExistException("Username is already taken. Please try again!");
	        }
	    } catch (IllegalArgumentException | UsernameAlreadyExistException e) {
	    	log.error("Either username {} and/or fields: name: {}, username: {}, password: {}  do not match the basic fields requirements", username, name, username, password);
	        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
	        redirectAttributes.addFlashAttribute("nameError", nameError); 
	        redirectAttributes.addFlashAttribute("usernameError", usernameError); 
	        redirectAttributes.addFlashAttribute("passwordError", passwordError); 
	        redirectAttributes.addFlashAttribute("name", name);
	        redirectAttributes.addFlashAttribute("username", username);
	        redirectAttributes.addFlashAttribute("password", password);
	        mv.setViewName("redirect:/api/v1/superadmins/createUser");
	        response.setStatus(HttpServletResponse.SC_CONFLICT);
	    }
	    return mv;
	}
	
	@GetMapping("/createManager")
	@PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
	public ModelAndView goToCreateManagerPage(HttpServletRequest request, HttpServletResponse response) {
		log.info("Entered into the /createManager request");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("createManager");
		log.info("Went to createManager.jsp page");
	    // Get flash attributes and add them to the model
	    Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
	    if (flashMap != null) {
	        mv.addObject("name", flashMap.get("name"));
	        mv.addObject("username", flashMap.get("username"));
	        mv.addObject("password", flashMap.get("password"));
	    }
	    response.setStatus(HttpServletResponse.SC_OK);
		return mv;
	}
    
	@PostMapping("/registerManager")
	@PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
	public ModelAndView createManager(@RequestParam String name,
	                                @RequestParam String username,
	                                @RequestParam String password,
	                                HttpServletRequest request,
	                                HttpServletResponse response,
	                                RedirectAttributes redirectAttributes) {
	    log.info("Entered into the /registerManager request");
	    ModelAndView mv = new ModelAndView();
	    String nameError = "";
	    String usernameError = "";
	    String passwordError = "";

	    try {
	        // Perform form validation
	        nameError = validateName(name);
	        usernameError = validateManagerUsername(username);
	        passwordError = validatePassword(password);

	        String errorMessage = validateManagerForm(name, username, password);
	        if (!errorMessage.isEmpty()) {
	            throw new IllegalArgumentException(errorMessage);
	        }

	        if (!managerService.verifyExistingUsername(username)) {
	            Manager manager = new Manager();
	            manager.setManagerId(new ObjectId());
	            manager.setName(name);
	            manager.setUsername(username);
	            manager.setPassword(password);

	            managerService.createManager(manager);

	            // Add success message attribute
	            redirectAttributes.addFlashAttribute("successMessage", "Manager created successfully.");
	            // Redirect to filtered view based on the username
	            mv.setViewName("redirect:/api/v1/superadmins/createManager");
	            response.setStatus(HttpServletResponse.SC_OK);
	            return mv;
	        } else {
	            log.error("Username is already taken. Please try again!");
	            throw new UsernameAlreadyExistException("Username is already taken. Please try again!");
	        }
	    } catch (IllegalArgumentException | UsernameAlreadyExistException e) {
	    	log.error("Either username {} and/or fields: name: {}, username: {}, password: {}  do not match the basic fields requirements", username, name, username, password);
	        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
	        redirectAttributes.addFlashAttribute("nameError", nameError); 
	        redirectAttributes.addFlashAttribute("usernameError", usernameError); 
	        redirectAttributes.addFlashAttribute("passwordError", passwordError); 
	        redirectAttributes.addFlashAttribute("name", name);
	        redirectAttributes.addFlashAttribute("username", username);
	        redirectAttributes.addFlashAttribute("password", password);
	        mv.setViewName("redirect:/api/v1/superadmins/createManager");
	        response.setStatus(HttpServletResponse.SC_CONFLICT);
	    }
	    return mv;
	}
}
