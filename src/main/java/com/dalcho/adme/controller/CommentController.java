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
    public Comment setComment(@ModelAttribute CommentDto commentDto) {
        return commentService.setComment(commentDto);
    }

    @GetMapping("/comment")
    public List<Comment> getComment(@RequestParam int idx){
        return commentService.getComment(idx);
    }

    // AuthenticationPrincipal : 로그인한 사용자의 정보를 파라메터로 받고 싶을때
    @PutMapping("/comment/{commentId}/registry/{registryId}")
    public Comment updateComment(@PathVariable Long commentId, @PathVariable int registryId,
                                 @RequestBody CommentDto commentDto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        return commentService.updateComment(commentId, registryId, commentDto, userDetails);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}/registry/{registryId}")
    public void deleteComment(@PathVariable Long commentId, @PathVariable int registryId,
                              @RequestBody CommentDto commentDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails)throws AccessDeniedException {
        commentService.deleteComment(commentId, registryId, commentDto, userDetails);
    }

    @GetMapping("/finduser")  // sessionStorage에 닉네임 값이 저장 안되어 있는 경우
    public String findUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.findUser(userDetails);
    }
}
