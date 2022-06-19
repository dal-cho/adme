package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final RegistryRepository registryRepository;

    @Transactional
    public Comment setComment(CommentDto commentDto) {
        Comment comment = new Comment(commentDto);
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> getComment(int idx){
        List<Comment> commentList = commentRepository.findAllByRegistryId(idx);
        return commentList;
    }

}
