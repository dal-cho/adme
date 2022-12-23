package com.dalcho.adme.security;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        return userRepository.findByUid(username);
    }
}