package com.abhay.springproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abhay.springproject.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	
	@Query(value="select * from user u where u.email=:email",nativeQuery = true)
	List<User> getData(@Param("email") String email);
	
	@Query(value="select * from user u where u.email=:email",nativeQuery = true)
	User getDataByMail(@Param("email") String email);
	
	@Query(value="select * from user u where u.phone=:phone",nativeQuery = true)
	List<User> getDataByPhone(@Param("phone") int phone);

	// User findByUsername(String email);

}
