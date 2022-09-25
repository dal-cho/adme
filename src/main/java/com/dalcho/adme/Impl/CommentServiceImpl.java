package com.dalcho.adme.Impl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final RegistryRepository registryRepository;

	@Transactional
	public Comment setComment(CommentDto commentDto) {
		Comment comment = new Comment(commentDto);
		commentRepository.save(comment);
		return comment;
	}

	public List<Comment> getComment(int idx){
		List<Comment> commentList = commentRepository.findAllByRegistryId(idx);
		return commentList;
	}

	@Transactional
	public Comment updateComment(Long commentId, int registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws
			AccessDeniedException {
		registryRepository.findById((long) registryId).orElseThrow(
				() -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
		);

		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
		);

		if (!userDetails.getUser().getNickname().equals(comment.getNickname()) ) {
			throw new AccessDeniedException("권한이 없습니다.");
		}

		comment.setComment(commentDto.getComment());
		commentRepository.save(comment);
		return comment;

	}

	@Transactional
	public void deleteComment(Long commentId, int registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws AccessDeniedException {
		registryRepository.findById((long) registryId).orElseThrow(
				() -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
		);

		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
		);

		if (!userDetails.getUser().getNickname().equals(comment.getNickname()) ) {
			throw new AccessDeniedException("권한이 없습니다.");
		}

		commentRepository.delete(comment);
	}
	// sessionStorage에 닉네임 값이 저장 안되어 있는 경우
	public String findUser(UserDetailsImpl userDetails) {
		return userDetails.getUser().getNickname();
	}

}

