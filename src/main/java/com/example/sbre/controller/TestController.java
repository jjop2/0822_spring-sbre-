package com.example.sbre.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.sbre.domain.TestVO;

@RestController
public class TestController {
	
	@GetMapping("/test")
	public String test() {
		return "Hello React & Spring";
	}
	
	@GetMapping("/test/info")
	public TestVO info() {
		// DB에서 가져온 정보라고 칩시다
		TestVO vo = new TestVO("qwer", "1234", 20);
		
		return vo;
	}
	
	@PostMapping("/test/{no}")
	public void test2(@PathVariable int no, @RequestBody TestVO vo, String msg) {
		
		System.out.println(no);
		System.out.println(vo.toString());
		System.out.println(msg);
		
	}
	
}
