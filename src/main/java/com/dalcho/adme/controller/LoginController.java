package com.dalcho.adme.controller;

import com.dalcho.adme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class LoginController {
    @GetMapping("/user/login/nickname") // session에 저장하기
    public Object sessionRequest(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpSession session){
        session.setAttribute("nickname", userDetails.getUser().getNickname());
        return session.getAttribute("nickname");
    }
}
