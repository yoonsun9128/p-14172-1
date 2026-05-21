package com.back.doamin.post.post.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc //MockMvc 를 자동으로 설정 , postman처럼 요청을 보낼 수 있음
@Transactional
public class ApiV1PostContollerTest {
	@Autowired
	private MockMvc mvc; //mockmvc를 주입받는다. == postman

	//글 작성 테스트
	@Test
	@DisplayName("글 쓰기")
	void t1() throws Exception {
		ResultActions resultActions = mvc.perform(
				post("/api/v1/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
									"title":"테스트 제목",
									"content":"테스트 내용"
								}
								""")
		).andDo(print());

		resultActions.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("글 수정")
	void t2() throws Exception {
		ResultActions resultActions = mvc.perform(
				put("/api/v1/posts/2")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
									"title":"테스트 제목 수정",
									"content":"테스트 내용 수정"
								}
								""")
		).andDo(print());
		resultActions.andExpect(status().isOk());
	}
}
