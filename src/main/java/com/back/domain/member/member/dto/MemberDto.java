package com.back.domain.member.member.dto;

import com.back.domain.member.member.entity.Member;

public record MemberDto (
		int id,
		String username,
		String name
) {
	public MemberDto(Member member) {
		this(
				member.getId(),
				member.getUsername(),
				member.getName()
		);
	}
}
