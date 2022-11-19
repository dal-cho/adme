package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRegistryId(Long idx);
}
