package com.example.sbre.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.sbre.domain.Board;

@RestController
public class BoardController {

	@PostMapping("/board")
	public ResponseEntity<?> insertBoard(@RequestBody Board board) {
		
		System.out.println(board);
		
		return null;
	}
}
