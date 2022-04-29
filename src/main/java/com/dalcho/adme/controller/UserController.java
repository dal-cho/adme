package com.dalcho.adme.controller;

import com.dalcho.adme.dto.SignupRequestDto;
import com.dalcho.adme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    // userService 생성자 생성
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "error"; //login
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "login";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(SignupRequestDto requestDto) {
        userService.registerUser(requestDto);
        return "redirect:/adme";
    }


    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }

    @GetMapping("/10s") // 10s 페이지
    public String ten(Model model) {
        model.addAttribute("data","data");
        return "tenseconds";
    }

    @GetMapping("/slide") // space 페이지
    public String space(Model model) {
        model.addAttribute("data","data");
        return "empathy-space";
    }

}
