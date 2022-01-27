package com.dalcho.login.repository;

import com.dalcho.login.domain.Registry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
}
