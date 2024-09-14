package com.StudentManagement.rest;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.StudentManagement.dao.StudentRepository;
import com.StudentManagement.dto.AuthenticationResponse;
import com.StudentManagement.dto.signInRequest;
import com.StudentManagement.entity.Student;
import com.StudentManagement.service.JwtService;
import com.StudentManagement.service.RoleService;
import com.StudentManagement.entity.Role;
import com.StudentManagement.service.courseService;
import com.StudentManagement.service.studentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Controller
public class AppContoller {

	private studentService studentService;

	private RoleService theRoleService;
	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtService jwtService;

	public AppContoller() {
	}

	@Autowired
	public AppContoller(studentService thestudentservice, RoleService roleservice) {
		studentService = thestudentservice;
		theRoleService = roleservice;
	}

	// home page
	@GetMapping("/")
	public String viewHomePage() {
		return "index";
	}

	// show login page
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("student", new Student());
		return "login";
	}

	// show sign up form
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("student", new Student());
		return "signup";
	}

	// show sign up form
	/*
	 * @GetMapping("/registerUser") public String showRegistrationFormUser(Model
	 * model) { model.addAttribute("user", new Role()); return "signUpUser"; }
	 */

	// actual register in DB for student
	@PostMapping("/process_register")
	public String processRegister(@ModelAttribute Student student) {
		Role role = new Role();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(student.getPassword());
		student.setPassword(encodedPassword);
		student.setEnabled(1);
		// role.setEnabled(1);
		role.setUsername(student.getUsername());
		role.setRole("STUDENT");
		System.out.println("saving students" + student + "\n");

		studentService.save(student);
		System.out.println("saving role" + role + "\n");
		theRoleService.save(role);

		return "register_success"; // dummy success page for successful registration
	}

	// actual register in DB for user
	/*
	 * @PostMapping("/process_registerUser") public String
	 * processRegister(@ModelAttribute Role user) { BCryptPasswordEncoder
	 * passwordEncoder = new BCryptPasswordEncoder(); String encodedPassword =
	 * passwordEncoder.encode(user.getPassword());
	 * user.setPassword(encodedPassword); user.setEnabled(1);
	 * theUserService.save(user);
	 * 
	 * return "register_success"; //dummy success page for successful registration }
	 */

	@PostMapping("/signin")
	public ResponseEntity<AuthenticationResponse> signin(@RequestBody signInRequest signInRequest) {
		System.out.println("ssss\n");
		String username = signInRequest.getUsername();
		String password = signInRequest.getPassword();
		authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		var user = studentService.findByUsername(username);

		System.out.println("before generate token in /authenticateTheUser in the controller\n");
		var jwt = jwtService.generateToken(user);
		var refreshJwt = jwtService.generateRefreshToken(new HashMap<>(), user);
		System.out.println(
				"after generate token in /authenticateTheUser in the controller\n" + "generated token" + jwt + "\n");
		System.out.println("after generate token in /authenticateTheUser in the controller\n"
				+ "generated refresh token" + refreshJwt + "\n");
		AuthenticationResponse authResponse = new AuthenticationResponse();
		authResponse.setToken(jwt);
		authResponse.setRefreshToken(refreshJwt);
		return new ResponseEntity<>(authResponse, HttpStatus.OK);

	}

//	@PostMapping("/signin")
//	public String signin(@RequestParam String username, @RequestParam String password, HttpServletRequest request,
//			RedirectAttributes redirectAttributes) {
//
//		try {
//			// Authenticate the user
//			Authentication authentication = authManager
//					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//
//			// Find the user and generate tokens
//			var user = studentService.findByUsername(username);
//			var jwt = jwtService.generateToken(user);
//			var refreshJwt = jwtService.generateRefreshToken(new HashMap<>(), user);
//			System.out.println("token is " + jwt+"\n");
//			// Store the JWT token in the session
//			HttpSession session = request.getSession();
//			session.setAttribute("jwtToken", jwt);
//			session.setMaxInactiveInterval(600); // 600 seconds = 10 minutes session expiry
//
//			// Redirect to the main page after login
//			return "redirect:/home"; // Replace with your home page or another view
//
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("error", "Invalid username or password");
//			return "redirect:/login"; // Redirect to login form on failure
//		}
//	}

}
