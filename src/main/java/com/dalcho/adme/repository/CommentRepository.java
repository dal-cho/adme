package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRegistry_Idx(Long idx);

    @Query("select c.registry.idx from Comment c")
    ArrayList<Long> findAllByRegistry_Idx();
}
