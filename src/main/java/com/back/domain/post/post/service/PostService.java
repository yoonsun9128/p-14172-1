package com.back.domain.post.post.service;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.repository.PostRepository;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.globalExceptionHandler.MemberUnexistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	public long count() {
		return postRepository.count();
	}

	public Post write(String title, String content, int id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(MemberUnexistException::new);
		Post post = new Post(title, content, member);

		return postRepository.save(post);
	}

	public Optional<Post> findById(int id) {
		return postRepository.findById(id);
	}

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public void modify(Post post, String title, String content, int id) {
//		Member member = memberRepository.findById(id)
//				.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
//
//		if (member.getId() != post.getAuthor().getId()) {
//			throw new RuntimeException("작성자가 달라 수정이 불가능합니다.");
//		}
		memberRepository.findById(id).ifPresent(
				a -> {
					if (a.getId() != post.getAuthor().getId()) {
						throw new RuntimeException("작성자가 달라 수정이 불가능합니다.");
					}
				}
		);
		post.modify(title, content);
	}

	public PostComment writeComment(Post post, String content, int id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(MemberUnexistException::new);
		return post.addComment(content, member);
	}

	public boolean deleteComment(Post post, PostComment postComment, int id) {
		memberRepository.findById(id).ifPresent(
				a -> {
					if (a.getId() != post.getAuthor().getId()) {
						throw new RuntimeException("작성자가 달라 삭제가 불가능합니다.");
					}
				}
		);
		return post.deleteComment(postComment);
	}

	public void modifyComment(PostComment postComment, String content) {
		postComment.modify(content);
	}

	public void delete(Post post, int id) {
		memberRepository.findById(id).ifPresent(
				a -> {
					if (a.getId() != post.getAuthor().getId()) {
						throw new RuntimeException("작성자가 달라 삭제가 불가능합니다.");
					}
				}
		);
		postRepository.delete(post);
	}

	public Optional<Post> findLastest() {
		return postRepository.findFirstByOrderByIdDesc();
	}

	public void flush() {
		postRepository.flush();
	}
}
