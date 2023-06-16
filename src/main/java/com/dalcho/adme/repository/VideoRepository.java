package com.dalcho.adme.repository;

import com.dalcho.adme.domain.VideoFile;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoFile, Long> {
    Page<VideoFile> findAll(Pageable pageable);
    Optional<VideoFile> findById(Long id);


    @Query("SELECT v FROM VideoFile v WHERE v.user.nickname = :nickname")
    Page<VideoFile> findByNickname(@Param("nickname") String nickname, Pageable pageable);
}
