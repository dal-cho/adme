package com.dalcho.login.repository;

import com.dalcho.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

