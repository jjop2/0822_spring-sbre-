package com.example.sbre.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sbre.domain.Board;
import com.example.sbre.domain.BoardDTO;
import com.example.sbre.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;

	public void insertBoard(Board board) {
		boardRepository.save(board);
	}
	
	public List<Board> getBoardList() {
		return boardRepository.findAllByOrderByIdDesc();
	}
	
	public BoardDTO getBoard(Integer id) {
		Board findBoard = boardRepository.findById(id).get();
		
		return new BoardDTO(findBoard);
	}
	
	public void updateBoard(Integer id, Board board) {
		Board originBoard = boardRepository.findById(id).get();
		
		originBoard.setTitle(
			board.getTitle() != null && !board.getTitle().isEmpty()
				? board.getTitle()
				: originBoard.getTitle()
		);
		originBoard.setContent(
			board.getContent() != null && !board.getContent().isEmpty()
				? board.getContent()
				: originBoard.getContent()
		);
		
		boardRepository.save(originBoard);
	}
	
	public void deleteBoard(Integer id) {
		boardRepository.deleteById(id);
	}
	
}
