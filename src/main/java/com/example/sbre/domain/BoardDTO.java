package com.example.sbre.domain;

import lombok.Data;

@Data
public class BoardDTO {

	private Integer id;
	private String title;
	private String content;
	private String writer;
	
	public BoardDTO(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.writer = board.getWriter().getUsername();
	}
	
	
	
}
