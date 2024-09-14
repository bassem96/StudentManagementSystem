package com.StudentManagement.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;


public interface JwtService {
	 String generateToken(UserDetails userDetails);

	 String extractUserName(String token);

	boolean isTokenValid(String jwt, UserDetails userDetails);
	String generateRefreshToken(Map<String,Object>Claims,UserDetails userDetails);
}
