package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.security.UserDetailsImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CommentService {

    Comment setComment(CommentDto commentDto);

    List<Comment> getComment(int idx);

   Comment updateComment(Long commentId, int registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws AccessDeniedException;

    void deleteComment(Long commentId, int registryId, CommentDto commentDto, UserDetailsImpl userDetails) throws AccessDeniedException;

    String findUser(UserDetailsImpl userDetails);
}
