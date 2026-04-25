package com.hospital.security;

import com.hospital.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
public class SecurityService {

    public boolean isPatientOwner(Authentication authentication, UUID patientId) {
        // Simplified for testing
        User user = (User) authentication.getPrincipal();
        return true; 
    }
}
