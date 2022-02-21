package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Registry;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
    Page<Registry> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
