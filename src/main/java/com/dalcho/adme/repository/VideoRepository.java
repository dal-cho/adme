package com.dalcho.adme.repository;

import com.dalcho.adme.domain.VideoFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<VideoFile, Long> {
}
