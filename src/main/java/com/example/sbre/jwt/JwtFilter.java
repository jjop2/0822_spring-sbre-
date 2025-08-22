package com.example.sbre.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = jwtService.resolveToken(request);
		boolean check = jwtService.validate(jwt);
		
		if(check) {
			// 토큰이 유효할 때
			// 사용자 정보(username, 권한) 꺼내기
			String username = jwtService.getUsername(jwt);
			List<? extends GrantedAuthority> roles = jwtService.getAuthorities(jwt);
			
			// 이미 인증되었으므로 매니저에게 보내는 과정 없이 바로 인증객체 생성
			// 두번째는 비밀번호인데 필요없으니 null
			Authentication auth = new UsernamePasswordAuthenticationToken(username, null, roles);
			
			// 인증된 사용자로 시큐리티에 등록
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		
		filterChain.doFilter(request, response);
	}
	
	
	
}
