package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRegistry_Idx(Long idx);

    @Query("SELECT r.idx FROM Registry r LEFT JOIN Comment c ON r.idx = c.registry.idx WHERE c.registry.idx is null")
    List<Long> findTop15By(Pageable pageable);
}
