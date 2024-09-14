package com.StudentManagement.service;

import java.util.List;

import com.StudentManagement.entity.Course;

public interface courseService {
	 List<Course> getAllCourses();
	 void registerCourse(Long studentId, Long courseId);
	 void cancelCourse(Long studentId, Long courseId);
}
