package com.dalcho.adme.service;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto postComment(CommentRequestDto commentDto);

    List<CommentResponseDto> getComment(Long idx);

    Integer getCountComment(Long idx);

    CommentResponseDto updateComment(Long commentId, CommentRequestDto commentDto, User user);

    void deleteComment(Long commentId, User user) ;

    List<RegistryResponseDto> needComments();
}
