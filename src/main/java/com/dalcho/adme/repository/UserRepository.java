package com.dalcho.adme.repository;

import com.dalcho.adme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
    //Optional<User> findByPasswordAndpassword_confirm(String password, String password_confirm);
}

