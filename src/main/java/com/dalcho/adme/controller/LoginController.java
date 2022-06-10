package com.dalcho.adme.controller;

import com.dalcho.adme.dto.SignupRequestDto;
import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final UserService userService;

    @GetMapping("/user/login/nickname") // session에 저장하기
    public Object sessionRequest(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpSession session){
        session.setAttribute("nickname", userDetails.getUser().getNickname());
        return session.getAttribute("nickname");
    }

    // id 중복 확인
    @PostMapping("/user/signup/username")
    public String checkId(@ModelAttribute SignupRequestDto requestDto) {
        return userService.checkId(requestDto);
    }

    // username 중복 확인
    @PostMapping("/user/signup/nickname")
    public String checkNickname(@ModelAttribute SignupRequestDto requestDto) {
        return userService.checkNickname(requestDto);
    }

    // password 중복 확인
    @PostMapping("/user/signup/password")
    public String checkPassword(@ModelAttribute SignupRequestDto requestDto) {
        return userService.checkPasswordConfirm(requestDto);
    }

    // 회원 가입 페이지
//    @PostMapping("/user/signup")
//    public String signUp(@ModelAttribute SignupRequestDto requestDto) {
//        return userService.signUp(requestDto);
//    }

}
