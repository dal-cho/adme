package com.dalcho.adme.controller;

import com.dalcho.adme.dto.sign.SignInRequestDto;
import com.dalcho.adme.dto.sign.SignInResultDto;
import com.dalcho.adme.dto.sign.SignUpRequestDto;
import com.dalcho.adme.dto.sign.SignUpResultDto;
import com.dalcho.adme.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class SignController {

    private final SignService signService;

    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("[SignController]");
        log.info("[signUp] 회원가입을 수행합니다. id : {}, password : ****, name : {}", signUpRequestDto.getNickname(), signUpRequestDto.getName());

        SignUpResultDto signUpResultDto = signService.signUp(signUpRequestDto);

        return signUpResultDto;
    }

    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(@RequestBody SignInRequestDto signInRequestDto) throws RuntimeException {
        log.info("[SignController]");
        log.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", signInRequestDto.getNickname());

        SignInResultDto signInResultDto = signService.signIn(signInRequestDto);

        if (signInResultDto.getCode() == 0) {
            log.info("[signIn] 정상적으로 로그인 되었습니다. id : {}", signInRequestDto.getNickname());
        }

        return signInResultDto;
    }

    @GetMapping("/user")
    public UserDetails userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("[SignController]");
        log.info("[userInfo] 유저정보 조회");
        return userDetails;
    }
}
