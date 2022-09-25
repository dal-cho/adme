package com.dalcho.adme.service;

import com.dalcho.adme.dto.LoginDto;
import com.dalcho.adme.dto.SignupRequestDto;
import com.dalcho.adme.domain.User;

public interface UserService {

    User registerUser(SignupRequestDto requestDto);

    // id 중복 체크
    String checkId(SignupRequestDto requestDto);


    String checkNickname(SignupRequestDto requestDto);


    // 비밀번호 확인 체크
    String checkPasswordConfirm(SignupRequestDto requestDto);

    // 회원가입
    String signUp(SignupRequestDto requestDto);


    // 로그인 페이지
    String login(LoginDto loginDto);

}