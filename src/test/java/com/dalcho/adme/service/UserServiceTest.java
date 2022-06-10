package com.dalcho.adme.service;

import com.dalcho.adme.dto.SignupRequestDto;
import com.dalcho.adme.model.User;
import com.dalcho.adme.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dalcho.adme.model.UserRole.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith( SpringExtension. class )
@SpringBootTest ( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
@Transactional
class UserServiceTest {
    @Autowired UserService userService;

    @Test
    void id중복테스트() throws Exception {

        SignupRequestDto requestDto1 = new SignupRequestDto();
        requestDto1.setUsername("dkj");
        requestDto1.setNickname("dkj");
        requestDto1.setPassword("dkj");
        requestDto1.setEmail("dkj");
        requestDto1.setPasswordConfirm("dkj");


        SignupRequestDto requestDto2 = new SignupRequestDto();
        requestDto2.setUsername("dkj");
        requestDto2.setNickname("dkj");
        requestDto2.setPassword("dkj");
        requestDto2.setEmail("dkj");
        requestDto2.setPasswordConfirm("dkj");


        //when
        userService.registerUser(requestDto1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(requestDto2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("중복된 사용자 ID 가 존재합니다.");


    }
}