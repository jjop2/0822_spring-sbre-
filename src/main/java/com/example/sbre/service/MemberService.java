package com.example.sbre.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sbre.domain.Member;
import com.example.sbre.domain.RoleType;
import com.example.sbre.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	public void insert(Member member) {
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		member.setRole(RoleType.USER);
		
		memberRepository.save(member);
	}
	
	public Member getMember(String username) {
		return memberRepository.findByUsername(username).get();
	}
	
}
