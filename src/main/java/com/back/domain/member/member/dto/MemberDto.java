package com.back.domain.member.member.dto;

import com.back.domain.member.member.entity.Member;

public record MemberDto (
	String username,
	String name
) {
	public MemberDto(Member member) {
		this(
				member.getUsername(),
				member.getName()
		);
	}
}
