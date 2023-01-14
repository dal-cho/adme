package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final RegistryRepository registryRepository;
    private final UserRepository userRepository;

    @Override
    public Comment postComment(CommentRequestDto commentDto) {
        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        User user = userRepository.findByNickname(commentDto.getNickname()).orElseThrow(UserNotFoundException::new);
        Comment comment = commentDto.toEntity(registry, user);
        Comment save = commentRepository.save(comment);
        return save;
    }

    @Override
    public List<CommentResponseDto> getComment(Long idx) throws NullPointerException {
        List<Comment> commentList = commentRepository.findAllByRegistry_Idx(idx);
        List<CommentResponseDto> resCommentDtoList = new LinkedList<>();
        try {
            for (int i = 0; i < commentList.size(); i++) {
                CommentResponseDto resCommentDto = CommentResponseDto.builder()
                        .registryNickname(commentList.get(0).getRegistry().getUser().getNickname())
                        .commentName(commentList.get(i).getUser().getNickname())
                        .comment(commentList.get(i).getComment())
                        .commentId(commentList.get(i).getIdx())
                        .build();
                resCommentDtoList.add(resCommentDto);
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
    public Comment updateComment(Long commentId, CommentRequestDto commentDto, User user){
        registryRepository.findById(commentDto.getRegistryIdx()).orElseThrow(RegistryNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!user.getNickname().equals(comment.getUser().getNickname())) {
            throw new InvalidPermissionException();
        }
        comment.updateComment(commentDto.getComment());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public void deleteComment(Long commentId, CommentRequestDto commentDto, User user) {
        registryRepository.findById(commentDto.getRegistryIdx()).orElseThrow(RegistryNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!user.getNickname().equals(comment.getUser().getNickname())) {
            throw new InvalidPermissionException();
        }
        commentRepository.delete(comment);
    }

    @Override
    public String findUser(User user) {// sessionStorage에 닉네임 값이 저장 안되어 있는 경우
        return user.getNickname();
    }

    @Override
    public List<Optional<Registry>> needComments() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "idx");
        List<Long> temp = commentRepository.findTop10By(pageable);
        List<Optional<Registry>> result = new ArrayList<>();
        for (int i = 0; i < temp.toArray().length; i++) {
            result.add(registryRepository.findById(temp.get(i)));
        }
        return result;
    }
}

