package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Registry;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
    // 생성 날짜 순으로 보여주기(desc : 내림차순)
    Page<Registry> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
