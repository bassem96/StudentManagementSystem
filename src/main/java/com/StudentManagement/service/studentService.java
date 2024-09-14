package com.StudentManagement.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.StudentManagement.entity.Student;

public interface studentService {
	void save(Student student);

	Student findByUsername(String username);
	
	UserDetailsService userDetailsService() ;
}
