package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.dto.response.ResCommentDto;
import com.dalcho.adme.security.UserDetailsImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment postComment(CommentDto commentDto);

    List<ResCommentDto> getComment(Long idx);

    Comment updateComment(Long commentId, Long registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws AccessDeniedException;

    void deleteComment(Long commentId, Long registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws AccessDeniedException;

    String findUser(UserDetailsImpl userDetails);

    List<Optional<Registry>> needComments();
}
