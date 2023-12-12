package com.dalcho.adme.controller;

import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import com.dalcho.adme.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    // 댓글 저장
    @PostMapping("/comment")
    public CommentResponseDto postComment(@RequestBody CommentRequestDto commentDto) {
        return commentService.postComment(commentDto);
    }

    @GetMapping("/comment")
    public List<CommentResponseDto> getComment(@RequestParam Long idx){
        return commentService.getComment(idx);
    }

    // AuthenticationPrincipal : 로그인한 사용자의 정보를 파라메터로 받고 싶을때
    @PutMapping("/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId,
                                 @RequestBody CommentRequestDto commentDto,
                                 @AuthenticationPrincipal UserDetails userDetails){
        return commentService.updateComment(commentId, commentDto, userDetails);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId,
                              @AuthenticationPrincipal UserDetails userDetails){
        commentService.deleteComment(commentId, userDetails);
    }

    @GetMapping("/side-registry")
    public List<RegistryResponseDto> needComments() {
        return commentService.needComments();
    }
}
