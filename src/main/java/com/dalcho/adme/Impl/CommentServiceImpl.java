package com.dalcho.adme.Impl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final RegistryRepository registryRepository;

    @Transactional
    public Comment postComment(CommentDto commentDto) {
        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        Comment comment = commentDto.toEntity(registry);
        Comment save = commentRepository.save(comment);
        return save;
    }

    public List<Comment> getComment(Long idx) {
        List<Comment> commentList = commentRepository.findAllByRegistry_Idx(idx);
        return commentList;
    }

    @Transactional
    public Comment updateComment(Long commentId, Long registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws
            AccessDeniedException {
        registryRepository.findById(registryId).orElseThrow(
                () -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );

        if (!userDetails.getUser().getNickname().equals(comment.getNickname())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        comment.updateComment(commentDto.getComment());
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId, Long registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws AccessDeniedException {
        registryRepository.findById(registryId).orElseThrow(
                () -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );

        if (!userDetails.getUser().getNickname().equals(comment.getNickname())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // sessionStorage에 닉네임 값이 저장 안되어 있는 경우
    public String findUser(UserDetailsImpl userDetails) {
        return userDetails.getUser().getNickname();
    }


    public List<Optional<Registry>> needComments() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> temp = commentRepository.findTop10By(pageable);
        List<Optional<Registry>> result = new ArrayList<>();
        for (int i = 0; i < temp.toArray().length; i++) {
            result.add(registryRepository.findById(temp.get(i)));
        }
        return result;
    }
}

