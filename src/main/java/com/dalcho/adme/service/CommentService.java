package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentResponseDto postComment(CommentRequestDto commentDto);

    List<CommentResponseDto> getComment(Long idx);

    Integer getCountComment(Long idx);

    CommentResponseDto updateComment(Long commentId, CommentRequestDto commentDto, User user) throws AccessDeniedException;

    void deleteComment(Long commentId, CommentRequestDto commentDto, User user) throws AccessDeniedException;

    List<RegistryResponseDto> needComments();
}
