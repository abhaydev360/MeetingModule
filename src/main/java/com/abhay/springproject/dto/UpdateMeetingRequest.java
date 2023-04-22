package com.abhay.springproject.dto;

import java.sql.Timestamp;
import java.util.List;

import com.abhay.springproject.Entity.Member;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class UpdateMeetingRequest {
	
	
	private Integer id;
	private int organiserId;
	private String title;
	private Timestamp start;
	private Timestamp end;
	private String loc;
	private String meetingstatus;
	private String organiser;
	private String link;
	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
//	@JoinColumn(name="mm_fk",referencedColumnName="id")
	@JsonIdentityReference(alwaysAsId = true)
	private List<Member> members;

}
