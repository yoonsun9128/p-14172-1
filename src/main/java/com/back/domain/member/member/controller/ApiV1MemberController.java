package com.back.domain.member.member.controller;

import com.back.domain.member.member.dto.MemberDto;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.dto.PostDto;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "ApiV1MemberController", description = "API 사용자 컨트롤러")
public class ApiV1MemberController {
	private final MemberService memberService;

	public record MemberWriteReqBody(
			@NotBlank
			@Size(min = 2, max= 30)
			String username,
			@NotBlank
			@Size(min = 2, max= 50)
			String password,
			@NotBlank
			@Size(min = 2, max= 30)
			String name
	) {}

	@PostMapping("/join")
	@Operation(summary = "회원 가입")
	public RsData<MemberDto> write(
			@Valid
			@RequestBody MemberWriteReqBody form
	) {
		Member newMember = memberService.write(form.username, form.password, form.name);

		return new RsData<>(
				"201-1",
				"%d번 사용자가 생성되었습니다.".formatted(newMember.getId()),
				new MemberDto(newMember)
		);
	}

	@GetMapping("/me")
	@Operation(summary = "내 정보 조회")
	public MemberDto getMemberInfo(
			@RequestParam(value = "actorId") int id
	) {
		Member member = memberService.findById(id).get();
		return new MemberDto(member);
	}
}
