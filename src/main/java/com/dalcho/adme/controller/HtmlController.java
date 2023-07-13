package com.dalcho.adme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {

    // 로그인 & 회원가입 페이지
    @GetMapping("/user/login")
    public String login(){
        return "login";
    }

    @GetMapping("/user/login/error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/space")
    public String registry(){
        return "empathy-space";
    }

    // 기본 페이지
    @GetMapping("/chat")
    public String home() {
        return "chat";
    }

    // 10s 페이지
    @GetMapping("/tenseconds")
    public String tenSeconds() {
        return "everyone-record";
    }


    @GetMapping("/taste") // 로그인 없이 이용가능한 페이지
    public String taste() {
        return "blur";
    }

    @GetMapping("/error") // 로그인 없이 이용가능한 페이지
    public String error() {
        return "error";
    }
}