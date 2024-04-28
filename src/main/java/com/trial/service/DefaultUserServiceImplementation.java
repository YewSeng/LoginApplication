package com.trial.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.trial.pojo.Manager;
import com.trial.pojo.User;
import com.trial.repository.ManagerRepository;
import com.trial.repository.UserRepository;
import lombok.Data;

@Data
@Service
public class DefaultUserServiceImplementation implements DefaultUserService {

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    
    @Value("${superadmin.secretKey}")
    private String superAdminSecretKey;
    
    @Autowired
    public DefaultUserServiceImplementation(UserRepository userRepository, 
    		ManagerRepository managerRepository) {
    	this.userRepository = userRepository;
    	this.managerRepository = managerRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if the user is a SuperAdmin -> They dont have a username and password
        if (superAdminSecretKey.equals(username)) {
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_SUPERADMIN");
            return new org.springframework.security.core.userdetails.User(
            		"Super Admin",
                    "",
                    authorities
            );
        }

        if (username.startsWith("U")) {
        	// Check if the user is in the User repository
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
                return new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        authorities
                );
            }
        } else if (username.startsWith("M")) {
            // Check if the user is in the Manager repository
            Optional<Manager> optionalManager = managerRepository.findByUsername(username);
            if (optionalManager.isPresent()) {
                Manager manager = optionalManager.get();
                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_MANAGER");
                return new org.springframework.security.core.userdetails.User(
                		manager.getUsername(),
                		manager.getPassword(),
                        authorities
                );
            }
        }
        throw new UsernameNotFoundException("User/Manager not found with username: " + username);
    }
}
