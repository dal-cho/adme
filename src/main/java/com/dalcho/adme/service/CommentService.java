package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.dto.response.ResCommentDto;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment postComment(CommentDto commentDto);

    List<ResCommentDto> getComment(Long idx);

    ResCommentDto getCountComment(Long idx);

    Comment updateComment(Long commentId, CommentDto commentDto, User user) throws AccessDeniedException;

    void deleteComment(Long commentId, CommentDto commentDto, User user) throws AccessDeniedException;

    String findUser(User user);

    List<Optional<Registry>> needComments();
}
