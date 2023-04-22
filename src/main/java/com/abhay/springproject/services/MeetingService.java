package com.abhay.springproject.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.Member;
import com.abhay.springproject.Entity.User;
import com.abhay.springproject.dto.EndMeetingRequest;
import com.abhay.springproject.dto.JoinMeeting;
import com.abhay.springproject.dto.StartMeeting;
import com.abhay.springproject.dto.UpdateMeetingRequest;
import com.abhay.springproject.repository.MeetingRepository;
import com.abhay.springproject.repository.UserRepository;

@Component
public class MeetingService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtService jwtService;

	@Autowired
	MeetingRepository meetingRepository;

	public String validateOrganiser(String token) {

		String username = jwtService.extractUsername(token);
		return username;

	}

	public boolean startMeeting(StartMeeting request) {
		Meeting meeting = meetingRepository.findById(request.getMeetingId()).orElse(null);
		if (meeting.getOrganiserId() != request.getOrganiserId()) {
			return false;
		}
		meeting.setMeetingstatus("Started");
		/*List<Member> member = meeting.getMembers();
		 *
		 *
		 *        
		 * for (Member member2 : member) { //Send Notification that meeting is started }
		 */

		
		User organiser = userRepository.findById(request.getOrganiserId()).orElse(null);
		organiser.setAvailability("InMeeting");
		meetingRepository.save(meeting);
		userRepository.save(organiser);
		return true;
	}

	public boolean joinMeeting(JoinMeeting request) {
		Meeting meeting = meetingRepository.findById(request.getMeetingId()).orElse(null);
		List<Member> member = meeting.getMembers();
		
		 // List<Integer> md = // .int id = request.getMemberId();
				// member.contains(request)) &&
		// if (((meeting.getMeetingstatus() == "Started"))) {
		
      		if (member.contains(request.getMid()) && meeting.getMeetingstatus() == "Started") {

			// swager
			// Join Meeting
      			
			User user = userRepository.findById(request.getMid()).orElse(null);
			user.setAvailability("InMeeting");
			userRepository.save(user);

			return true;

		}

		return false;
	}

//	public boolean validateMember()

	public boolean validateMeetingTime(Timestamp start, Timestamp end, int id) {

		List<Meeting> listOfMeeting = meetingRepository.validateMeetingTime(start, end, id);

		// Meeting data = meetingRepository.validateMeeting(start,id);
		if (!listOfMeeting.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}
	/* public String validateMember(String) */

	public boolean updateMeeting(UpdateMeetingRequest request) {
		Meeting meeting = meetingRepository.findById(request.getId()).orElse(null);

		if (meeting == null) {

			return false;
		}
		List<Member> oldMember = meeting.getMembers();
		List<Member> requestedMember = request.getMembers();
		if (!meeting.getTitle().equals(request.getTitle())) {
			meeting.setTitle(request.getTitle());
		}
		if (meeting.getStart().compareTo(request.getStart()) != 0) {
			meeting.setStart(request.getStart());
		}
		if (meeting.getEnd().compareTo(request.getEnd()) != 0) {
			meeting.setEnd(request.getEnd());
		}

		List<Member> tempMember = new ArrayList<>();
		for (Member rqmem : requestedMember) {
			int count = 0;
			for (Member oldmem : oldMember) {

				if (rqmem.getMid() == oldmem.getMid()) {
					count = 1;
					break;
				}
			}
			if (count == 0) {
				rqmem.setMeeting(meeting);
				tempMember.add(rqmem);
			}

		}

		meeting.setMembers(tempMember);
		meetingRepository.save(meeting);

		return true;
	}

	public boolean endMeeting(EndMeetingRequest request) {
		// TODO Auto-generated method stub

		// User user= userRepository.findById(request.getUserId()).orElse(null);
		Meeting findById = meetingRepository.findById(request.getMeetingId()).orElse(null);
		if (findById.getOrganiserId() == request.getUserId()) {
			meetingRepository.deleteById(request.getMeetingId());
			return true;
		}
		return false;

	}

}
