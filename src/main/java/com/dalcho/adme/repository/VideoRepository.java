package com.dalcho.adme.repository;

import com.dalcho.adme.domain.VideoFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<VideoFile, Long> {
    Page<VideoFile> findAll(Pageable pageable);
}
