package com.abhay.springproject.dto;

import lombok.Data;


public class TokenResponse {
	
	private String token;
	private String name;
	private String role;
	
	public TokenResponse(String token , String name,String role) {

		this.name=name;
		this.role=role;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
