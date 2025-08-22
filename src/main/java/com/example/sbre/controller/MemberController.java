package com.example.sbre.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.sbre.domain.Member;
import com.example.sbre.jwt.JwtService;
import com.example.sbre.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody Member member) {
		memberService.insert(member);
		
		return new ResponseEntity<>("회원가입 성공!", HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Member member) {
		// UsernamePasswordAuthenticationToken 토큰 만들기
		UsernamePasswordAuthenticationToken cred = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
		
		// 매니저한테 만든 위 토큰을 보내주면 교재의 4~6 과정을 함. 즉, 인증 객체를 만듦
		// 그렇게 만들어진 인증객체를 우리는 지금 auth 변수에 담음
		// Authentication -> import : org.springframework.security.core
		Authentication auth = authenticationManager.authenticate(cred);
		
		// 인증객체 auth를 서비스로 보내 토큰을 생성
		String jwt = jwtService.createToken(auth.getName(), auth.getAuthorities());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
				.build();
		
	}
	
	@GetMapping("/userinfo")
	// 로그인한 정보는 지금 인증객체(JwtFilter-doFilterInternal) 안에 있음
	public ResponseEntity<?> userInfo(Authentication auth) {
		Member member = memberService.getMember(auth.getName());
		
		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	
}
