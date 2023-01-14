package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment postComment(CommentRequestDto commentDto);

    List<CommentResponseDto> getComment(Long idx);

    Integer getCountComment(Long idx);

    Comment updateComment(Long commentId, CommentRequestDto commentDto, User user) throws AccessDeniedException;

    void deleteComment(Long commentId, CommentRequestDto commentDto, User user) throws AccessDeniedException;

    String findUser(User user);

    List<Optional<Registry>> needComments();
}
