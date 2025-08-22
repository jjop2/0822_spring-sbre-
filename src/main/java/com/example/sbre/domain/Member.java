package com.example.sbre.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	@Id
	private String username;
	private String password;
	private String email;
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
}
