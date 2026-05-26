package com.back.doamin.post.post.controller;

import com.back.domain.post.post.controller.ApiV1PostController;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc //MockMvc 를 자동으로 설정 , postman처럼 요청을 보낼 수 있음
@Transactional
public class ApiV1PostContollerTest {
	@Autowired
	private MockMvc mvc; //mockmvc를 주입받는다. == postman
	@Autowired
	private PostService postService;

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

		Post post = postService.findLastest().get();
		long totalCount = postService.count();

		//좀더 깐깐하게 검증 시도
		resultActions
				.andExpect(handler().handlerType(ApiV1PostController.class))
				.andExpect(handler().methodName("write"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.resultCode").value("201-1"))
				.andExpect(jsonPath("$.msg").value("%d번 글이 생성되었습니다.".formatted(post.getId())))
				.andExpect(jsonPath("$.data.id").value(post.getId()))
				.andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(post.getCreateDate().toString().substring(0, 20))))
				.andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(post.getModifyDate().toString().substring(0, 20))))
				.andExpect(jsonPath("$.data.title").value("테스트 제목"))
				.andExpect(jsonPath("$.data.content").value("테스트 내용"));
//				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("글 수정")
	void t2() throws Exception {
		int id = 1;
		ResultActions resultActions = mvc.perform(
				put("/api/v1/posts/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
									"title":"테스트 제목 수정",
									"content":"테스트 내용 수정"
								}
								""")
		).andDo(print());
		resultActions
				.andExpect(handler().handlerType(ApiV1PostController.class))
				.andExpect(handler().methodName("modify"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resultCode").value("200-1"))
				.andExpect(jsonPath("$.msg").value("%d번 글이 수정되었습니다.".formatted(id)));

		Post post = postService.findById(id).get();

		assertThat(post.getTitle()).isEqualTo("테스트 제목 수정");
		assertThat(post.getContent()).isEqualTo("테스트 내용 수정");
	}
}
