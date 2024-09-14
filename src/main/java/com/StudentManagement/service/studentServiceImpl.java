package com.StudentManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.StudentManagement.dao.CourseRepository;
import com.StudentManagement.dao.StudentRepository;
import com.StudentManagement.entity.Student;

@Service
public class studentServiceImpl implements studentService {

	private StudentRepository studentRepo;

	public studentServiceImpl() {
	}

	@Autowired
	public studentServiceImpl(StudentRepository theStudentRepository) {
		studentRepo = theStudentRepository;
	}

	@Override
	public void save(Student student) {

		studentRepo.save(student);
	}

	@Override
	public Student findByUsername(String username) {
		return studentRepo.findByUsername(username);
	}

	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return studentRepo.findByUsername(username);
			}
		};
	}

}