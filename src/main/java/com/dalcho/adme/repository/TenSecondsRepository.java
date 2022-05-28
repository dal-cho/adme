package com.dalcho.adme.repository;

import com.dalcho.adme.domain.TenSeconds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenSecondsRepository extends JpaRepository<TenSeconds, Long> {
    List<TenSeconds> findAllByUserId(Long userId);
}
