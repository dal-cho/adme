package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRegistry_Idx(Long idx);
}
