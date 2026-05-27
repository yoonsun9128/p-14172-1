package com.back.domain.post.post.controller;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "ApiV1PostController", description = "API 글 컨트롤러")
public class ApiV1PostController {
	private final PostService postService;

	@GetMapping
	@Operation(summary = "다건 조회")
	public List<PostDto> getItems() {
		List<Post> items = postService.findAll();

		return items
				.stream()
				.map(PostDto::new)
				.toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "단건 조회")
	public PostDto getItem(
			@PathVariable int id
	) {
		Post post = postService.findById(id).get();
		return new PostDto(post);
	}

	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "삭제")
	public RsData<Void> delete(
			@PathVariable int id,
			@RequestParam(value = "actorId") int memberId
	) {
		Post post = postService.findById(id).get();

		postService.delete(post, memberId);
		return new RsData<>(
				"200-1",
				"%d번 글이 삭제되었습니다.".formatted(id)
		);
	}

	public record PostWriteReqBody(
			@NotBlank
			@Size(min = 2, max = 100)
			String title,
			@NotBlank
			@Size(min = 2, max = 5000)
			String content
	) {
	}

	public record PostWriteResBody(
			long totalCount,
			PostDto post
	) {
	}

	@PostMapping
	@Transactional
	@Operation(summary = "작성")
	public RsData<PostDto> write(@Valid @RequestBody PostWriteReqBody form, @RequestParam(value = "actorId") int id) {
		Post post = postService.write(form.title, form.content, id);

//		PostWriteResBody data = new PostWriteResBody(postService.count(), new PostDto(post));

		return new RsData<>(
				"201-1",
				"%d번 글이 생성되었습니다.".formatted(post.getId()),
				new PostDto(post)
		);
	}

	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "수정")
	public RsData<PostWriteResBody> modify(
			@PathVariable int id,
			@Valid @RequestBody PostWriteReqBody form
	) {
		Post post = postService.findById(id).get();
		postService.modify(post, form.title, form.content);

		return new RsData<>(
				"200-1",
				"%d번 글이 수정되었습니다.".formatted(post.getId())
		);
	}

}
