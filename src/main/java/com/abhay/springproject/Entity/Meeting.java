package com.abhay.springproject.Entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

@NamedNativeQuery(name = "Meeting.getMeetingList", query = "SELECT m.title as title, m.loc as loc, m.organiser as organiser, m.link as link FROM meeting m WHERE m.start = :start and m.end=:end", resultSetMapping = "Mapping.MeetingListReq")
@SqlResultSetMapping(name = "Mapping.MeetingListReq", classes = @ConstructorResult(targetClass = MeetingListReq.class, columns = {
		@ColumnResult(name = "title"), @ColumnResult(name = "loc"), @ColumnResult(name = "organiser"),
		@ColumnResult(name = "link")

}))

public class Meeting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
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
