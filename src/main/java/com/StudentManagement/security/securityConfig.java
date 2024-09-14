package com.StudentManagement.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.UserDetailsManager;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.StudentManagement.config.JwtAuthenticationFilter;
import com.StudentManagement.service.studentService;

@Configuration
@EnableWebSecurity
public class securityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private studentService theStudentService;

	/*
	 * @Bean public UserDetailsManager userDetailsManager(DataSource dataSource) {
	 * 
	 * JdbcUserDetailsManager jdbcUserDetailsManager = new
	 * JdbcUserDetailsManager(dataSource);
	 * 
	 * // define query to retrieve a user by username jdbcUserDetailsManager
	 * .setUsersByUsernameQuery("select username,password,enabled from students where username=?"
	 * );
	 * 
	 * // define query to retrieve the authorities/roles by username
	 * jdbcUserDetailsManager.
	 * setAuthoritiesByUsernameQuery("select username, role from roles where username=?"
	 * );
	 * 
	 * System.out.println("usermanager created" + jdbcUserDetailsManager + "\n");
	 * return jdbcUserDetailsManager; }
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	/*SecurityFilterChain used for form authentication using basic authetication*/
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
				configurer -> configurer.requestMatchers("/").permitAll().requestMatchers("/register").permitAll()
						.requestMatchers("/login").permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/authenticateTheUser").permitAll())
				.logout(logout -> logout.permitAll())
				.exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"));

		return http.build();
	}

	/*SecurityFilterChain used for jwt authentication*/
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		System.out.println("in filterChain");
//		http.authorizeHttpRequests(
//				configurer -> configurer.requestMatchers("/").permitAll().requestMatchers("/register").permitAll()
//						.requestMatchers("/process_register").permitAll().requestMatchers("/signin").permitAll()
//						.requestMatchers("/login").permitAll().anyRequest().authenticated())
//				.csrf(csrf -> csrf.disable())
//				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.authenticationProvider(authenticationProvider())
//				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//				// .formLogin(form ->
//				// form.loginPage("/login").loginProcessingUrl("/authenticate").permitAll())
//				.formLogin(form -> form.loginPage("/login") // Redirect to login page if not authenticated
//						.successHandler(savedRequestAwareAuthenticationSuccessHandler()) // Redirect to original URL
//																							// after login
//						.permitAll())
//				.logout(logout -> logout.permitAll())
//				.exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"));
//		System.out.println();
//		return http.build();
//	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		System.out.println("inside authenticationProvider");
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(theStudentService.userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		System.out.println("inside authenticationManager");
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
		// This handler redirects the user back to the original URL after successful
		// login
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setDefaultTargetUrl("/index"); // Default URL after login if no original request is found
		return successHandler;
	}
}