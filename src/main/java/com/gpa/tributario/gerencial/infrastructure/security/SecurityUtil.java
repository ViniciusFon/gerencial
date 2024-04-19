package com.gpa.tributario.gerencial.infrastructure.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil(){

    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserDetailsImpl getUserData() {
        return (UserDetailsImpl) getAuthentication().getPrincipal();
    }

    public static String getIdentification() {
        return getUserData().getUsername();
    }

}
