package com.abhay.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhay.springproject.Entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

}
