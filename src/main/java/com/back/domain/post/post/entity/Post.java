package com.back.domain.post.post.entity;

import com.back.domain.member.member.entity.Member;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {
	private String title;
	private String content;
	@ManyToOne
	private Member author;

	//orphanRemoval -> transactional에 존속되어야 삭제가 가능하다.
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	private List<PostComment> comments = new ArrayList<>();

	public Post(String title, String content, Member author) {
		this.title =  title;
		this.content =  content;
		this.author = author;
	}

	public void modify(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public PostComment addComment(String content, Member member) {
		PostComment postComment = new PostComment(this, content, member);
		comments.add(postComment);

		return postComment;
	}

	public Optional<PostComment> findCommentById(int id) {
		return comments
				.stream()
				.filter(comment -> comment.getId() == id)
				.findFirst();
	}

	public boolean deleteComment(PostComment postComment) {
		if (postComment == null) return false;

		return comments.remove(postComment);
	}
}
