package com.back.domain.member.member.service;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public long count() {
		return memberRepository.count();
	}

	public Member write(String username, String password, String name) {
		Member member = new Member(username, password, name);
		return memberRepository.save(member);
	}

	public Optional<Member> findLastTet() {
		return memberRepository.findFirstByOrderByIdDesc();
	}
}
