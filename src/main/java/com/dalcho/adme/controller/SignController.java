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

        log.info("[signUp] 회원가입을 완료했습니다. id : {}", signUpRequestDto.getNickname());

        return signUpResultDto;
    }

    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(@RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response) throws RuntimeException {
        log.info("[SignController]");
        log.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", signInRequestDto.getNickname());

        SignInResultDto signInResultDto = signService.signIn(signInRequestDto);

        if (signInResultDto.getCode() == 0) {
            log.info("[signIn] 정상적으로 로그인되었습니다. id : {}", signInRequestDto.getNickname());
        }

        log.info("[getSignInResult] 쿠키 생성"); // 쿠키에 시간 정보를 주지 않으면 세션 쿠키가 된다. (브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("TokenCookie", signInResultDto.getToken());
        idCookie.setPath("/"); // 모든 경로에서 접근 가능
        idCookie.setDomain("localhost");
        idCookie.setMaxAge(24 * 60 * 60); // 1day
        idCookie.setSecure(true);
        response.addCookie(idCookie);

        log.info("[getSignInResult] 쿠키 생성 완료");

        return signInResultDto;
    }

    @GetMapping("/user")
    public UserDetails userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("[SignController]");
        log.info("[userInfo] 유저정보 조회");
        return userDetails;
    }
}
