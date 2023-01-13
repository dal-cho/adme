package com.dalcho.adme.repository;

import com.dalcho.adme.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}

