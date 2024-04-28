package com.trial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptService {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public BcryptService() {
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
