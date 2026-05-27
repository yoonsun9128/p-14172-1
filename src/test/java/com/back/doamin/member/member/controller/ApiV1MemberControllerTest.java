package com.back.doamin.member.member.controller;

import com.back.domain.member.member.controller.ApiV1MemberController;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiV1MemberControllerTest {
	@Autowired
	private MockMvc mvc; //mockmvc를 주입받는다. == postman
	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("회원가입")
	void t1() throws Exception {
		ResultActions resultActions = mvc.perform(
				post("/api/v1/members/join")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								    "username" : "user",
								    "password" : "1234",
								    "name" : "azalea"
								}
								""")
		).andDo(print());

		Member member = memberService.findLastTet().get();
		resultActions
				.andExpect(handler().handlerType(ApiV1MemberController.class))
				.andExpect(handler().methodName("write"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.resultCode").value("201-1"))
				.andExpect(jsonPath("$.msg").value("%d번 사용자가 생성되었습니다.".formatted(member.getId())))
				.andExpect(jsonPath("$.data.id").value(member.getId()))
				.andExpect(jsonPath("$.data.username").value("user"))
				.andExpect(jsonPath("$.data.name").value("azalea"));
	}

	@Test
	@DisplayName("회원 중복 에러")
	void t2() throws Exception {
		ResultActions resultActions = mvc.perform(
				post("/api/v1/members/join")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								    "username" : "user1",
								    "password" : "1234",
								    "name" : "유저1"
								}
								""")
		).andDo(print());

		resultActions
				.andExpect(handler().handlerType(ApiV1MemberController.class))
				.andExpect(handler().methodName("write"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.resultCode").value("409-1"));
	}

	@Test
	@DisplayName("회원 조회")
	void t3() throws Exception {
		int id = 3;
		ResultActions resultActions = mvc.perform(
				get("/api/v1/members/me?actorId=" + id)
		).andDo(print());

		Member member = memberService.findById(id).get();

		resultActions
				.andExpect(handler().handlerType(ApiV1MemberController.class))
				.andExpect(handler().methodName("getMemberInfo"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(member.getId()))
				.andExpect(jsonPath("$.username").value("user1"))
				.andExpect(jsonPath("$.name").value("유저1"));
	}
}
