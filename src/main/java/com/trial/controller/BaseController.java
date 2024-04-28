package com.trial.controller;

import java.util.Collections;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.trial.config.JwtGeneratorValidator;
import com.trial.service.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Data
@NoArgsConstructor
public class BaseController {
	
	@Autowired
	private JwtGeneratorValidator jwtValidator;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Value("${jwt.secretKey}")
    private String SECRET;
	
    @Value("${superadmin.secretKey}")
    private String superAdminKey;
    
    private String validateLoginFormUsername(String username) {
        if (!username.matches("^(M|U)[a-zA-Z0-9]{7,}$")) {
            return "Username must start with an uppercase 'M' or 'U' followed by at least 7 characters";
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
    
    private String validateSuperAdminKey(String superAdminKey) {
    	String superAdminKeyRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    	if (superAdminKey == null || superAdminKey.trim().isEmpty()) {
    		return "Super Admin Key cannot be empty!";
    	} else if (!superAdminKey.matches(superAdminKeyRegex)) {
    		return "Invalid Super Admin Key Pattern!";
    	}
    	return "";
    }
    
    private String validateLoginForm(String username, String password) {
        // Regular expressions for validation
    	String usernameRegex = "^(M|U)[a-zA-Z0-9]{7,}$"; // Username must start with "M" or "U" (upper case) and be followed by at least 7 characters and contain only letters and numbers
        String passwordRegex = "^(?=.*[A-Z])(?=.*[@])(?=.*[0-9])[a-zA-Z0-9@]{8,}$"; // Password: At least 1 uppercase, 1 "@", 1 number, At least length 8

        // Validation checks
        StringBuilder errorMessage = new StringBuilder();

        if (!username.matches(usernameRegex)) {
            errorMessage.append("Username must start with an uppercase 'M' or 'U' followed by at least 7 characters\n");
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
    
    private String extractPersonDetailsFromToken(HttpServletRequest request) {
    	String token = extractTokenFromRequest(request);
        if (token != null) {
            try {
                // Parse the token to extract claims
                Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
                
                if (claims.containsKey("role") && claims.containsKey("sub")) {
                    String role = (String) claims.get("role");
                    String username = (String) claims.get("sub");
                    log.info("Role: {}", role);
                    log.info("Username: {}", username);                   
                    return "[" + role + "]" + username;
                }
            } catch (JwtException e) {
                // Handle JWT parsing exceptions
                log.error("Error parsing JWT token: {}", e.getMessage());
                return "";
            }
        }
        return "";
    }
    
    private void removeTokens(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token") || cookie.getName().equals("refreshToken")) {                	
                	cookie.setMaxAge(0);
                	cookie.setPath("/");
                	response.addCookie(cookie);
                }
            }
        }
    }

    
	@GetMapping("/")
	public ModelAndView goToIndexPage(HttpServletRequest request, HttpServletResponse response) {
		log.info("Entered into the / request");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		log.info("Went to Index.jsp page");
	    response.setStatus(HttpServletResponse.SC_OK);
		return mv;
	}	
	
	@PostMapping("/login")
	public ModelAndView genericAuthenticationForm(
	    @RequestParam String username,
	    @RequestParam String password,
	    HttpServletRequest request,
	    HttpServletResponse response,
	    RedirectAttributes redirectAttributes) {

	    log.info("Entered into the /login request");
	    ModelAndView mv = new ModelAndView();
	    boolean isUser = false;
	    boolean isManager = false;
	    
	    try {
	        // Perform form validation
	    	String usernameError = validateLoginFormUsername(username);
	        String passwordError = validatePassword(password);
	        String errorMessage = validateLoginForm(username, password);
	        
	        if (!errorMessage.isEmpty()) {
	            redirectAttributes.addFlashAttribute("username", username);
	            redirectAttributes.addFlashAttribute("password", password);
	            redirectAttributes.addFlashAttribute("usernameError", usernameError);
	            redirectAttributes.addFlashAttribute("passwordError", passwordError);
	            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
	            redirectAttributes.addFlashAttribute("loginErrorMessage", "Invalid userid or password");
	            mv.setViewName("redirect:"); 
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            return mv;
	        } else {
	            isUser = authenticationService.authenticateUser(username, password);
	            isManager = authenticationService.authenticateManager(username, password);
	            log.info("Attempting to authenticate as a User: {}", isUser);
	            log.info("Attempting to authenticate as a Manager: {}", isManager);

	            // Authenticate using Spring Security
	            Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(username, password));
	            log.info("Authentication: {}", authentication);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            
	            if (isUser || isManager) {
	                // Generate JWT tokens
	                String token = jwtValidator.generateToken(authentication);
	                String refreshToken = jwtValidator.generateRefreshToken(authentication);

	                // Store tokens in cookies
	                Cookie tokenCookie = new Cookie("token", token);
	                tokenCookie.setPath("/");
	                response.addCookie(tokenCookie);
	                
	                Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
	                refreshTokenCookie.setPath("/");
	                response.addCookie(refreshTokenCookie);
	                
	                // Redirect based on user role
	                if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
	                    mv.setViewName("redirect:/api/v1/managers/home");
	                } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
	                	mv.setViewName("redirect:/api/v1/users/home");
	                }
                    response.setStatus(HttpServletResponse.SC_OK);
                    return mv;
	            } else {
	                // Authentication failed
	                redirectAttributes.addFlashAttribute("username", username);
	                redirectAttributes.addFlashAttribute("password", password);
		            redirectAttributes.addFlashAttribute("usernameError", usernameError);
		            redirectAttributes.addFlashAttribute("passwordError", passwordError);
		            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
	                redirectAttributes.addFlashAttribute("loginErrorMessage", "Invalid userid or password");
	                mv.setViewName("redirect:");
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                return mv;
	            }
	        }
	    } catch(Exception e) {
	        log.error("Exception found: {}", e);
	        e.printStackTrace();
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("password", password);
	        redirectAttributes.addFlashAttribute("loginErrorMessage", "Invalid userid or password");
	        mv.setViewName("redirect:");
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        return mv;
	    }
	}
	
	@GetMapping("/hidden")
	public ModelAndView goToHiddenSuperAdminLoginForm(HttpServletRequest request, HttpServletResponse response) {
		log.info("Entered into the /hidden request");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("hidden");
		log.info("Went to hidden.jsp page");
	    response.setStatus(HttpServletResponse.SC_OK);
		return mv;
	}
	
	@PostMapping("/superAdminLogin")
	public ModelAndView authenticationSuperAdmin(@RequestParam String superAdminKey,
	                                              HttpServletRequest request,
	                                              HttpServletResponse response,
	                                              RedirectAttributes redirectAttributes) {
	    log.info("Entered into the /superAdminLogin request");
	    ModelAndView mv = new ModelAndView();
	    boolean isSuperAdmin = false;
	    try {
	    	String superAdminKeyError = validateSuperAdminKey(superAdminKey);
	    	if (!superAdminKeyError.isEmpty()) {
	    		redirectAttributes.addFlashAttribute("loginErrorMessage", "Invalid userid or password");
	    		mv.setViewName("redirect:/hidden");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            return mv;	
	    	} else {
                isSuperAdmin = authenticationService.authenticateSuperAdmin(superAdminKey);
                log.info("Attempting to authenticate as a Super Admin: {}", isSuperAdmin);
                // Authenticate using Spring Security
                Authentication authentication = authenticationManager.authenticate(
                		new UsernamePasswordAuthenticationToken(superAdminKey, null));
                log.info("Authentication: {}", authentication);
                
                if (isSuperAdmin) {
                    // Create authentication token with super admin details
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User("Super Admin", "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUPERADMIN")));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                    
                    // Set authentication in SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    // Log authentication status
                    log.info("Is Super Admin authenticated: {}", SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
                    // Generate JWT token
                    String token = jwtValidator.generateToken(authentication);
                    log.info("JWT TOKEN: {}", token);
                    // Generate JWT refresh token
                    String refreshToken = jwtValidator.generateRefreshToken(authentication);
                    log.info("JWT REFRESH TOKEN: {}", refreshToken);
                    // Store token in cookie
                    Cookie tokenCookie = new Cookie("token", token);
                    tokenCookie.setPath("/");
                    response.addCookie(tokenCookie);
                    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
                    refreshTokenCookie.setPath("/");
                    response.addCookie(refreshTokenCookie);
                    if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
                        mv.setViewName("redirect:/api/v1/superadmins/home");
                    } else {
                    	 mv.setViewName("redirect:/hidden");
                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                    return mv;
                } else {
                	redirectAttributes.addFlashAttribute("loginErrorMessage", "Invalid userid or password");
                	mv.setViewName("redirect:/hidden");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return mv;
                }
	    	}
		} catch(Exception e) {
	    	log.error("Exception found: {}", e);
	    	e.printStackTrace();
	    	mv.setViewName("redirect:/hidden");
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	return mv;
		}
	}
	
	@GetMapping("/logout")
	@PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_USER', 'ROLE_SUPERADMIN')")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, 
			RedirectAttributes redirectAttributes) {
		log.info("Entered into the /logout request");
		ModelAndView mv = new ModelAndView(); 

	    // Remove the token cookie
		removeTokens(request, response);
		
		String userDetails = extractPersonDetailsFromToken(request);
		redirectAttributes.addFlashAttribute("logoutMessage", userDetails + " have successfully logout");

		mv.setViewName("redirect:");
	    response.setStatus(HttpServletResponse.SC_OK);
	    return mv;
	}
}
