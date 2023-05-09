package com.dalcho.adme.controller;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
import com.dalcho.adme.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    // 댓글 저장
    @PostMapping("/comment")
    public CommentResponseDto postComment(@ModelAttribute CommentRequestDto commentDto) {
        return commentService.postComment(commentDto);
    }

    @GetMapping("/comment")
    public List<CommentResponseDto> getComment(@RequestParam Long idx){
        return commentService.getComment(idx);
    }

    @GetMapping("comment-count")
    public Integer getCountComment(@RequestParam Long idx){
        return commentService.getCountComment(idx);
    }

    // AuthenticationPrincipal : 로그인한 사용자의 정보를 파라메터로 받고 싶을때
    @PutMapping("/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId,
                                 @RequestBody CommentRequestDto commentDto,
                                 @AuthenticationPrincipal User user) throws AccessDeniedException {
        return commentService.updateComment(commentId, commentDto, user);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId,
                              @RequestBody CommentRequestDto commentDto,
                              @AuthenticationPrincipal User user)throws AccessDeniedException {
        commentService.deleteComment(commentId, commentDto, user);
    }

    @GetMapping("/needComment")
    public List<Optional<Registry>> needComments() {
        return commentService.needComments();
    }
}
