package com.back.global.globalExceptionHandler;

public class MemberUnexistException extends RuntimeException {
	public MemberUnexistException() {
		super("존재하지 않는 회원입니다.");
	}
}
