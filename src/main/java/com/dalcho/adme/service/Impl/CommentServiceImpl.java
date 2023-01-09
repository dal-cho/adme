package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.dto.response.ResCommentDto;
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

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final RegistryRepository registryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment postComment(CommentDto commentDto) {
        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        User user = userRepository.findByNickname(commentDto.getNickname()).orElseThrow(() -> {throw new RuntimeException();});
        Comment comment = commentDto.toEntity(registry, user);
        Comment save = commentRepository.save(comment);
        return save;
    }

    public List<ResCommentDto> getComment(Long idx) throws NullPointerException {
        List<Comment> commentList = commentRepository.findAllByRegistry_Idx(idx);
        ResCommentDto resCommentDto;
        List<ResCommentDto> resCommentDtoList = new LinkedList<>();
        try {
            for (int i = 0; i < commentList.size(); i++) {
                resCommentDto = ResCommentDto.builder()
                        .registryNickname(commentList.get(0).getRegistry().getUser().getNickname())
                        .commentName(commentList.get(i).getUser().getNickname())
                        .comment(commentList.get(i).getComment())
                        .commentId(commentList.get(i).getIdx())
                        .build();
                resCommentDtoList.add(resCommentDto);
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("[error] CommentServiceImpl의 getComment()에서 null이 포함 ↓  \n" + e.getMessage() + "\n");
        }
        return resCommentDtoList;
    }

    public Integer getCountComment(Long idx) {
        List<Comment> commentList = commentRepository.findAllByRegistry_Idx(idx);
        return commentList.size();
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentDto commentDto, User user) throws
            AccessDeniedException {
        registryRepository.findById(commentDto.getRegistryIdx()).orElseThrow(
                () -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );

        if (!user.getNickname().equals(comment.getUser().getNickname())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        comment.updateComment(commentDto.getComment());
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId, CommentDto commentDto, User user) throws AccessDeniedException {
        registryRepository.findById(commentDto.getRegistryIdx()).orElseThrow(
                () -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );

        if (!user.getNickname().equals(comment.getUser().getNickname())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // sessionStorage에 닉네임 값이 저장 안되어 있는 경우
    public String findUser(User user) {
        return user.getNickname();
    }


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

