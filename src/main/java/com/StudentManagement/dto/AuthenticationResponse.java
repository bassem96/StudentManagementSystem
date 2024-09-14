package com.StudentManagement.dto;

import lombok.Data;
/*Response object to return token and refresh as a json object*/
@Data
public class AuthenticationResponse {
	private String token;
	private String refreshToken;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
