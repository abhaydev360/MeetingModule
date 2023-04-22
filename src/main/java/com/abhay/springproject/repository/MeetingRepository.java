package com.abhay.springproject.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.MeetingListReq;
import com.abhay.springproject.Entity.Member;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

	
	public List<Member> findMembersById(int id);

	@Query(nativeQuery = true)
	List<MeetingListReq> getMeetingList(@Param("start") Date date, @Param("end") Date date2);

	
	@Query(value="select * from meeting m where m.organiser_id=:id and m.start=:start",nativeQuery = true)
	public Meeting validateMeeting(@Param("start") String string, @Param("id") int id);

	@Query(value="select * from meeting m where (m.start<=:start and m.end>=:start and m.organiser_id=:id) OR (m.start<:end and m.start>:start and m.organiser_id=:id) OR (m.end>:start and m.end<:end) ",nativeQuery = true)
	public List<Meeting> validateMeetingTime(@Param("start") Timestamp start, @Param("end") Timestamp end, int id);

}
