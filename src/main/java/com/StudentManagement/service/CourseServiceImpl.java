package com.StudentManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.StudentManagement.dao.CourseRepository;
import com.StudentManagement.dao.StudentRepository;
import com.StudentManagement.entity.Course;
import com.StudentManagement.entity.Student;


/*course service layer responsible for courses api by injecting CourseRepository and StudentRepository*/
@Service
public class CourseServiceImpl implements courseService {

	private CourseRepository courseRepository;
	private StudentRepository studentRepository;

	public CourseServiceImpl() {
	}

	@Autowired
	public CourseServiceImpl(CourseRepository thecourseRepository, StudentRepository theStudentRepository) {
		courseRepository = thecourseRepository;
		studentRepository = theStudentRepository;
	}

	@Override
	@Cacheable(value = "courses")
	public List<Course> getAllCourses() {
		System.out.println("fetching all courses from database \n");
		return courseRepository.findAll();
	}

	@Override
	@CacheEvict(value = "courses", allEntries = true)
	public void registerCourse(Long studentId, Long courseId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

		// Check if the student is already registered for the course
		if (!student.getCourses().contains(course)) {
			// Add the course to the student's list of courses
			student.getCourses().add(course);
			// Optionally: Add the student to the course's list of students (if
			// bidirectional)
			course.getStudents().add(student);
			// Save the updated student entity
			studentRepository.save(student);
		} else {
			throw new RuntimeException("Student is already registered for this course");
		}

	}

	@Override
	@CacheEvict(value = "courses", allEntries = true)
	public void cancelCourse(Long studentId, Long courseId) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

		if (student.getCourses().contains(course)) {
			student.getCourses().remove(course);
			course.getStudents().remove(student); // If the relationship is bidirectional
			studentRepository.save(student); // Save updated student
		} else {
			throw new RuntimeException("Student is not registered for this course");
		}

	}
}
