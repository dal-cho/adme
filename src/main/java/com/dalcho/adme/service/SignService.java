package com.dalcho.adme.service;

import com.dalcho.adme.dto.sign.SignInRequestDto;
import com.dalcho.adme.dto.sign.SignInResultDto;
import com.dalcho.adme.dto.sign.SignUpRequestDto;
import com.dalcho.adme.dto.sign.SignUpResultDto;

public interface SignService {
    SignUpResultDto signUp(SignUpRequestDto signUpRequestDto);

    SignInResultDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException;

}
