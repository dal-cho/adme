package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.UserRole;
import com.dalcho.adme.dto.sign.SignUpRequestDto;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.duplicate.UserDuplicateIdException;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.Impl.SignServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static com.dalcho.adme.exception.ErrorCode.USER_DUPLICATE_ID;
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
    @Mock
    PasswordEncoder passwordEncoder;
    User user;

    @Test
    void id중복테스트() throws Exception {
        // given
        SignUpRequestDto requestDto1 = new SignUpRequestDto();
        requestDto1.setNickname("dkj");
        requestDto1.setName("dkj");
        requestDto1.setPassword("dkj");
        requestDto1.setEmail("email@naver.com");
        //requestDto1.setPasswordConfirm("dkj");

        SignUpRequestDto requestDto2 = new SignUpRequestDto();
        requestDto2.setNickname("dkj");
        requestDto2.setName("dkj");
        requestDto2.setPassword("dkj");
        requestDto2.setEmail("email@naver.com");
        //requestDto2.setPasswordConfirm("dkj");

        //when
        when(userRepository.existsByNickname(any())).thenReturn(true);

        //then
        CustomException e = assertThrows(UserDuplicateIdException.class,
                () -> userService.signUp(requestDto2));
        assertEquals(USER_DUPLICATE_ID, e.getErrorCode());
        verify(userRepository).existsByNickname(any());
    }

    @Test
    @DisplayName("회원가입 test")
    void signUp() {
        //given
        PasswordEncoder passEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 실제 객체
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setNickname("끼까꿍");
        signUpRequestDto.setPassword("Hell0 coco!!");
        signUpRequestDto.setName("김철수");
        signUpRequestDto.setEmail("email@naver.com");

        UserRole role = UserRole.of(UserRole.USER.name());
        String pw = passEncoder.encode(signUpRequestDto.getPassword());
        user = User.builder()
                .nickname(signUpRequestDto.getNickname())
                .username(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .password(pw)
                .role(role)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        userService.signUp(signUpRequestDto);
        verify(userRepository).save(any(User.class));

        // then
        assertFalse(user.getUsername().isEmpty());
    }

//    @Test
//    @DisplayName("로그인 test")
//    void signIn() {
//        //given
//        signUp();
//        SignInRequestDto signInRequestDto = new SignInRequestDto();
//        signInRequestDto.setNickname("끼까꿍");
//        signInRequestDto.setPassword("뮤뮤");
//
//        when(userRepository.findByNickname(any())).thenReturn(Optional.ofNullable(user));
//        when(passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())).thenReturn(true);
//
//        //when
//        userService.signIn(signInRequestDto);
//        verify(userRepository).findByNickname(signInRequestDto.getNickname());
//
//        //then
//        //assertTrue(passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword()));
//        //assertNotEquals(signInRequestDto.getPassword(), encodedPassword);
//    }
}