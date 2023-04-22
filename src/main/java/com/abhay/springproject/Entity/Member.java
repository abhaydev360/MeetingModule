package com.abhay.springproject.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mid")
public class Member {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mid;
	private String mname;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mm_fk")
	@JsonIdentityReference(alwaysAsId = true)
	private Meeting meeting;
	
}
