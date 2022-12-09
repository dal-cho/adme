package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRegistry_Idx(Long idx);

    //SELECT r.idx FROM Registry r LEFT JOIN Comment ON r.idx = Comment.registry.idx WHERE Comment.registry.idx is null
    @Query(value = "SELECT r.registry_id FROM Registry r LEFT JOIN Comment ON r.registry_id = comment.registry_id WHERE Comment.registry_id is null Limit 10;", nativeQuery = true)
    List<Long> findTop10By();

}
