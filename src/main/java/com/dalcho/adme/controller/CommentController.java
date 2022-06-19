package com.dalcho.adme.controller;

import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.service.CommentService;
import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.dto.CommentDto;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    // 댓글 저장
    @PostMapping("/comment")
    public Comment setComment(CommentDto commentDto){
        return commentService.setComment(commentDto);
    }

    @GetMapping("/comment")
    public List<Comment> getComment(@RequestParam int idx){
        return commentService.getComment(idx);
    }
}
