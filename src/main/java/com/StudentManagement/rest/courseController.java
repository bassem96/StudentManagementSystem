package com.StudentManagement.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.StudentManagement.entity.Course;
import com.StudentManagement.entity.Student;
import com.StudentManagement.entity.StudentErrorResponse;
import com.StudentManagement.service.PDFService;
import com.StudentManagement.service.courseService;
import com.StudentManagement.service.studentService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class courseController {

	private courseService courseservice;
	private studentService studentService;
	private PDFService pdfService;

	public courseController() {
	}

	@Autowired
	public courseController(courseService thecourseservice, studentService theStudentService,
			PDFService thePDFService) {
		courseservice = thecourseservice;
		studentService = theStudentService;
		pdfService = thePDFService;
	}

	@GetMapping("/courses")
	public String getAllCourses(Model model) {
		List<Course> theCourses = courseservice.getAllCourses();
		model.addAttribute("courses", theCourses);
		// System.out.println(theCourses+"\n");
		// System.out.println("model"+model);
		return "courses";
	}

	@GetMapping("/RegisteredCourses")
	public String getRegisteredCourses(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Student student = studentService.findByUsername(username);

		List<Course> theCourses = student.getCourses();
		model.addAttribute("courses", theCourses);
		// System.out.println(theCourses+"\n");
		// System.out.println("model"+model);
		return "RegisteredCourses";
	}

	@GetMapping("/registerCourses")
	public String registerCourses(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Student student = studentService.findByUsername(username);

		
		List<Course> theCourses = courseservice.getAllCourses();
		model.addAttribute("student", student);
		model.addAttribute("courses", theCourses);
		System.out.println("courses are " + theCourses);
		return "registerCourses";
	}

	@PostMapping("/process_registerCourses")
	public String process_registerCourses(@RequestParam Long studentId, @RequestParam Long courseId,
			RedirectAttributes redirectAttributes) {
		try {
			courseservice.registerCourse(studentId, courseId);
			System.out.println("course added");
			// Add an attribute that will be available after the redirect
			redirectAttributes.addFlashAttribute("message", "Course registered successfully!");
			return "redirect:/api/registerCourses";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/api/registerCourses";
		}
	}

	@PostMapping("/process_cancelCourses")
	public String process_CancelCourses(@RequestParam Long studentId, @RequestParam Long courseId,
			RedirectAttributes redirectAttributes) {
		try {
			courseservice.cancelCourse(studentId, courseId);
			System.out.println("course removed");
			// Add an attribute that will be available after the redirect
			redirectAttributes.addFlashAttribute("message", "Course canceled successfully!");
			return "redirect:/api/registerCourses";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/api/registerCourses";
		}
	}

	@GetMapping("/generateCourseSchedulePdf")
	public ResponseEntity<ByteArrayResource> generateCourseSchedulePdf() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Student student = studentService.findByUsername(username);

		byte[] pdfBytes = pdfService.generateCourseSchedulePdf(student.getId());
		ByteArrayResource resource = new ByteArrayResource(pdfBytes);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=course_schedule.pdf")
				.contentType(MediaType.APPLICATION_PDF).contentLength(pdfBytes.length).body(resource);
	}

	@ExceptionHandler
	public ResponseEntity<StudentErrorResponse> handleException(Exception exc) {

		// create a StudentErrorResponse
		StudentErrorResponse error = new StudentErrorResponse();

		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());

		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
