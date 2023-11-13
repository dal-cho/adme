package com.dalcho.adme.service;

import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {

    CommentResponseDto postComment(CommentRequestDto commentDto);

    List<CommentResponseDto> getComment(Long idx);

    Integer getCountComment(Long idx);

    CommentResponseDto updateComment(Long commentId, CommentRequestDto commentDto, UserDetails userDetails);

    void deleteComment(Long commentId, UserDetails userDetails) ;

    List<RegistryResponseDto> needComments();
}
