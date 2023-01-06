package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.sign.SignInRequestDto;
import com.dalcho.adme.dto.sign.SignUpRequestDto;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.Impl.SignServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    SignServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Spy
    PasswordEncoder passwordEncoder;
    User user;

    @Test
    void id중복테스트() throws Exception {
        // given
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
        when(userRepository.existsByNickname(any())).thenReturn(true);

        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.signUp(requestDto2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("[getSignUpResult] 중복된 사용자 ID 가 존재합니다.");
        verify(userRepository).existsByNickname(any());
    }

//    @Test
//    @DisplayName("password 암호화 test")
//    void passwordConfirm() {
//        //given
//        String password = "12345678";
//
//        //when
//        String encodedPassword = passwordEncoder.encode(password);
//
//        //then
//        assertAll( // 2가지 test
//                () -> assertNotEquals(password, encodedPassword),
//                () -> assertTrue(passwordEncoder.matches(password, encodedPassword))
//        );
//    }

    @Test
    @DisplayName("회원가입 test")
    void signUp() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setNickname("끼까꿍");
        signUpRequestDto.setPassword("뮤뮤");
        signUpRequestDto.setName("김철수");

        List<String> role = Collections.singletonList("ROLE_USER");
        user = User.builder()
                .nickname(signUpRequestDto.getNickname())
                .name(signUpRequestDto.getName())
                .password(signUpRequestDto.getPassword())
                .roles(role)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        userService.signUp(signUpRequestDto);
        verify(userRepository).save(any(User.class));

        //then
        assertAll( // 2가지 test
                () -> assertEquals(signUpRequestDto.getNickname(), user.getNickname()),
                () -> assertEquals(signUpRequestDto.getName(), user.getName())
        );
    }

    @Test
    @DisplayName("로그인 test")
    void signIn() {
        //given
        signUp();
        SignInRequestDto signInRequestDto = new SignInRequestDto();
        signInRequestDto.setNickname("끼까꿍");
        signInRequestDto.setPassword("뮤뮤");

        when(userRepository.findByNickname(any())).thenReturn(user);
        String encodedPassword = passwordEncoder.encode(signInRequestDto.getPassword());

        //when
        userService.signIn(signInRequestDto);
        verify(userRepository).findByNickname(signInRequestDto.getNickname());

        //then
        //assertTrue(passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword()));
        assertNotEquals(signInRequestDto.getPassword(), encodedPassword);
    }
}