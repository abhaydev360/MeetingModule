package com.abhay.springproject.dto;

import lombok.Data;

@Data
public class ChangePass {

	private int id;
	private String oldPassword;
	private String newPassword;
}
