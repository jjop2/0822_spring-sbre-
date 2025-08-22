package com.example.sbre.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

	// 토큰의 만료 시간 저장 변수 ( 단위 :ms )
	static final long EXPIRATIONTIME = 24 * 60 * 60 * 1000;
	
	// 토큰 타입 (응답 헤더에 사용될 접두어)
	// Bearer 뒤에 스페이스바 한 칸 띄우기
	static final String PREFIX = "Bearer ";
	
	// 임시 서명 키
	static final SecretKey KEY = Jwts.SIG.HS256.key().build();
	
	// 권한 관련 클레임 키
	static final String ROLES_CLAIM = "roles";
	
	// Date -> import : java.util
	public String createToken(String username, Collection<? extends GrantedAuthority> authorities) { 
		Date now = new Date();
		// 토큰 만료 날짜 (현재 시간 + 토큰 만료 시간)
		Date exp = new Date( now.getTime() + EXPIRATIONTIME );
		
		List<String> roles = (authorities == null)
								? List.of()
								: authorities.stream()
										.map(GrantedAuthority::getAuthority)
										.toList();
		
		// 클레임들
		return Jwts.builder()
					.subject(username)	// sub
					.issuedAt(now)		// iat
					.expiration(exp)	// exp
					.signWith(KEY)		// 서명
					.claim(ROLES_CLAIM, roles) // 커스텀 클레임은 아래처럼 claim(키, 밸류)
					.compact();
	}
	
	/*
	 * 요청 객체에서 헤더의 Authorization에 있는 토큰을 추출하는 메서드
	 */
	public String resolveToken(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(header != null && header.startsWith(PREFIX)) {
			// 토큰 검증할 때 "Bearer "은 필요없으므로 자르기
			return header.substring(PREFIX.length()).trim();
		}
		
		return null;
	}
	
	/*
	 * 토큰 유효성 검사하는 메서드
	 */
	public boolean validate(String token) {
		if(token == null || token.isBlank())
			return false;
		
		try {
			Jwts.parser()
				.verifyWith(KEY) // 서명 지정
				.clockSkewSeconds(30) // (선택사항) 30초 정도 오차는 허용
				.build()
				.parseSignedClaims(token); // 검증
			
			return true;
		} catch (ExpiredJwtException e) {
			return false; // 유효기간 만료되었을 때
		} catch (JwtException e) {
			return false; // 서명, 형식 등등 검증 실패 시
		} catch (Exception e) {
			return false; // 기타 예외
		}
	}
	
	/*
	 * 토큰에서 subject(username) 꺼내주는 메서드
	 */
	public String getUsername(String token) {
		Claims claims = Jwts.parser()
							.verifyWith(KEY)
							.build()
							.parseSignedClaims(token)
							.getPayload(); // 실제 정보(클레임)들 들어있음
		return claims.getSubject();
	}
	
	/*
	 * 토큰에서 권한(role) 꺼내주는 메서드
	 */
	public List<? extends GrantedAuthority> getAuthorities(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(KEY)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
		Object raw = claims.get(ROLES_CLAIM);
		
		if(raw instanceof List<?> list) {
			return list.stream()
						.filter(String.class::isInstance)
						.map(String.class::cast)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
		}
		
		return List.of();
	}
}
