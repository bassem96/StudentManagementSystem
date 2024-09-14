package com.StudentManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

/*
 * enable caching to use Redis caching
 */
@SpringBootApplication(scanBasePackages = "com.StudentManagement")
@EnableCaching
public class StudentManagementApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StudentManagementApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

}
