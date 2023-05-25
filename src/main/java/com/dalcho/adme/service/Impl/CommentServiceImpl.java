package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import com.dalcho.adme.exception.db.DatabaseErrorException;
import com.dalcho.adme.exception.invalid.InvalidPermissionException;
import com.dalcho.adme.exception.notfound.CommentNotFoundException;
import com.dalcho.adme.exception.notfound.RegistryNotFoundException;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final RegistryRepository registryRepository;
    private final UserRepository userRepository;
    private static final int PAGE_POST_COUNT = 15;

    @Override
    public CommentResponseDto postComment(CommentRequestDto commentDto) {
        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        User user = userRepository.findByNickname(commentDto.getNickname()).orElseThrow(UserNotFoundException::new);
        Comment comment = commentDto.toEntity(registry, user);
        commentRepository.save(comment);
        return CommentResponseDto.of(comment);
    }

    @Override
    public List<CommentResponseDto> getComment(Long idx) throws NullPointerException {
        List<Comment> commentList = commentRepository.findAllByRegistry_Idx(idx);
        List<CommentResponseDto> resCommentDtoList = new ArrayList<>();
        try {
            for (int i = 0; i < commentList.size(); i++) {
                resCommentDtoList.add(CommentResponseDto.of(commentList.get(i)));
            }
        } catch (NullPointerException e) {
            throw new DatabaseErrorException();
        }
        return resCommentDtoList;
    }

    @Override
    public Integer getCountComment(Long idx) {
        List<Comment> commentList = commentRepository.findAllByRegistry_Idx(idx);
        return commentList.size();
    }

    @Override
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentDto, User user) {
        registryRepository.findById(commentDto.getRegistryIdx()).orElseThrow(RegistryNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!user.getNickname().equals(comment.getUser().getNickname())) {
            throw new InvalidPermissionException();
        }
        comment.updateComment(commentDto.getComment());
        commentRepository.save(comment);
        return CommentResponseDto.of(comment);
    }

    @Override
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!user.getNickname().equals(comment.getUser().getNickname())) {
            throw new InvalidPermissionException();
        }
        commentRepository.delete(comment);
    }

    @Override
    public List<RegistryResponseDto> needComments() {
        Pageable pageable = PageRequest.of(0, PAGE_POST_COUNT, Sort.Direction.ASC, "idx");
        List<Long> registryIds = commentRepository.findTop15By(pageable);
        List<Registry> registries = registryRepository.findAllById(registryIds);
        List<RegistryResponseDto> result = new ArrayList<>();
        for (Registry registry : registries) {
            result.add(RegistryResponseDto.of(registry));
        }
        return result;
    }
}

