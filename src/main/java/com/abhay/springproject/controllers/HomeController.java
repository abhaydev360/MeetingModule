package com.abhay.springproject.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.MeetingListReq;
import com.abhay.springproject.Entity.Member;
import com.abhay.springproject.Entity.User;
import com.abhay.springproject.dto.AuthRequest;
import com.abhay.springproject.dto.CancelMeetingReq;
import com.abhay.springproject.dto.ChangePass;
import com.abhay.springproject.dto.DeactReq;
import com.abhay.springproject.dto.EndMeetingRequest;
import com.abhay.springproject.dto.JoinMeeting;
import com.abhay.springproject.dto.ListReq;
import com.abhay.springproject.dto.MeetingList;
import com.abhay.springproject.dto.StartMeeting;
import com.abhay.springproject.dto.TokenResponse;
import com.abhay.springproject.dto.UpdateMeetingRequest;
import com.abhay.springproject.filter.JwtAuthFilter;
import com.abhay.springproject.repository.MeetingRepository;
import com.abhay.springproject.repository.UserRepository;
import com.abhay.springproject.services.JwtService;
import com.abhay.springproject.services.MeetingService;

@RestController
public class HomeController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	MeetingRepository meetingRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtService jwtService;
	@Autowired
	JwtAuthFilter filter;
	@Autowired
	MeetingService meetingService;         
	@Autowired
	BCryptPasswordEncoder bcrypt;

	@PostMapping("/admin/login")
	@ResponseBody
	public TokenResponse login(@RequestBody AuthRequest request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		if (authentication.isAuthenticated()) {
			String token = jwtService.generateToken(request.getEmail());
			User user = userRepository.getDataByMail(request.getEmail());
			return new TokenResponse(token, user.getName(), user.getRole());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

	@PostMapping("/admin/changePassword")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public String changePassword(@RequestBody ChangePass request) {
		System.out.println("inside Change Pass");
		User user = userRepository.findById(request.getId()).orElse(null);
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), request.getOldPassword()));
		System.out.println("Inside Change Pass");
		if (authentication.isAuthenticated()) {
			user.setPassword(bcrypt.encode(request.getNewPassword()));
			userRepository.save(user);
			return "Password Changed";
		}
		return "Password Not Changed";
	}

	@PostMapping("/admin/adduser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public String addUser(@RequestBody User user) {

		System.out.println("Inside addUser");
		user.setStatus(1);
		user.setPassword(bcrypt.encode(user.getPassword()));
		user.setAvailability("Available");
		userRepository.save(user);

		return "User Registered";
	}

	@GetMapping("/admin/listuser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public List<User> ListUsers(@RequestBody ListReq listReq) {

		if (listReq.getEmail() != null) {
			// return userRepository.findByEmail(listReq.getEmail());

			List<User> data = userRepository.getData(listReq.getEmail());
			return data;

		} else if (listReq.getPhone() != 0) {
			List<User> data = userRepository.getDataByPhone(listReq.getPhone());
			return data;

		}

		List<User> data = userRepository.findAll();
		return data;

	}

	@GetMapping("/admin/deactivateuser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String deactivateUser(@RequestBody DeactReq req) {
		System.out.println("Inside Deacivate");
		User user = userRepository.findById(req.getId()).orElse(null);
		user.setStatus(0);
		userRepository.save(user);
		System.out.println("Inside Deacivate");
		return "User Deactivated ";
	}

	
	
	
	
	@PostMapping("/user/startmeeting")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@ResponseBody
	public String startMeeting(@RequestBody StartMeeting request) {
		if (meetingService.startMeeting(request)) {
			return "Meeting Started";
		}
		return "Something Went Wrong";
	}

	@PostMapping("/user/endmeeting")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@ResponseBody
	public String endMeeting(@RequestBody EndMeetingRequest request) {
		 
		if(meetingService.endMeeting(request)) {
			return "Meeting ended";
		}
		
		return  "You are not an Organiser";
	}

	@PostMapping("/user/joinmeeting")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@ResponseBody
	public String joinMeeting(@RequestBody JoinMeeting request) {
		if (meetingService.joinMeeting(request)) {
			return "Joined";
		}

		return "something went wrong";
	}

	@PostMapping("/user/updatemeeting")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@ResponseBody
	public String updateMeeting(@RequestBody UpdateMeetingRequest request) {

		if (meetingService.updateMeeting(request)) {
			System.out.println("hello");
			return "Meeting Updated";
		}

		return "Something went wrong";
	}

	@PostMapping("user/createMeeting")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@ResponseBody
	public String createmeeting(@RequestBody Meeting request) {
		String username = meetingService.validateOrganiser(filter.getToken());
		User user = userRepository.getDataByMail(username);
		String name = user.getName();
		request.setOrganiser(name);
		request.setOrganiserId(user.getId());
		boolean timevalidation = meetingService.validateMeetingTime(request.getStart(), request.getEnd(), user.getId());
		if (!timevalidation) {
			return "Another Meeting is Scheduled in this Time frame";
		}
		List<Member> mem = request.getMembers();
		System.out.println(mem);
		for (Member val : mem) {
			val.setMeeting(request);
		}
		request.setMembers(mem);
		request.setMeetingstatus("Created");

		meetingRepository.save(request);
		return "Meeting Created";
 
	}

	@PostMapping("/user/meetingList")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN',ROLE_USER)")
	@ResponseBody
	
	public List<MeetingListReq> meetingList(@RequestBody MeetingList request) {
		System.out.println("Inside List");
		return meetingRepository.getMeetingList(request.getStart(), request.getEnd());

	}         
	
	
	@PostMapping("/user/cancelMeeting")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@ResponseBody
	public String cancelMeeting(@RequestBody CancelMeetingReq request) {
		Meeting data = meetingRepository.findById(request.getId()).orElseGet(null);
		String organiserByDefault = data.getOrganiser();
		String organiser = request.getOrganiser();
		if (organiser.equals(organiserByDefault)) {
			meetingRepository.deleteById(data.getId());
			return "Meeting Deleted";
		}
		return "Something went Wrong";
	}
}





