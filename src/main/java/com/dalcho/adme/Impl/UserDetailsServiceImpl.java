package com.dalcho.adme.Impl;

import com.dalcho.adme.repository.UserRepository;

import com.dalcho.adme.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // User 엔티티의 id 값 가져오기 (인증)
    @Override
    public UserDetails loadUserByUsername(String username){
        log.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username);
        return userRepository.findByNickname(username);
    }
}
