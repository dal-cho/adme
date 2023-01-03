package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.dto.sign.SignUpRequestDto;
import com.dalcho.adme.service.SignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserServiceTest {
    @InjectMocks
    SignService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void id중복테스트() throws Exception {

        SignUpRequestDto requestDto1 = new SignUpRequestDto();
        requestDto1.setNickname("dkj");
        requestDto1.setName("dkj");
        requestDto1.setPassword("dkj");
        //requestDto1.setEmail("dkj");
        //requestDto1.setPasswordConfirm("dkj");


        SignUpRequestDto requestDto2 = new SignUpRequestDto();
        requestDto2.setNickname("dkj");
        requestDto2.setName("dkj");
        requestDto2.setPassword("dkj");
        //requestDto2.setEmail("dkj");
        //requestDto2.setPasswordConfirm("dkj");


        //when
        userService.signUp(requestDto1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.signUp(requestDto2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("중복된 사용자 ID 가 존재합니다.");
    }

    @Test
    @DisplayName("password 암호화 test")
    void passwordConfirm() {
        //given
        String password = "12345678";

        //when
        String encodedPassword = passwordEncoder.encode(password);

        //then
        assertAll( // 2가지 test
                () -> assertNotEquals(password, encodedPassword),
                () -> assertTrue(passwordEncoder.matches(password, encodedPassword))
        );
    }
}