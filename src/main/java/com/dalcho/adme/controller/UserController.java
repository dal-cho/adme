package com.dalcho.adme.controller;

import com.dalcho.adme.dto.SignupRequestDto;
import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

//    @GetMapping("/user/login")
//    public String login(@RequestParam(value = "error", required = false) String error,
//                        @RequestParam(value = "exception", required = false)
//                                String exception, Model model) {
//        model.addAttribute("error", error);
//        model.addAttribute("exception", exception);
//        return "login";
//    }

    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "error"; //login
    }

    // 회원 가입 페이지
//    @GetMapping("/user/signup")
//    public String signup() {
//        return "login";
//    }

    // 회원 가입 요청 처리
//    @PostMapping("/user/signup")
//    public String registerUser(SignupRequestDto requestDto) {
//        userService.registerUser(requestDto);
//        return "redirect:/";
//    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("nickname", userDetails.getUser().getNickname());
        return "index";
    }
    

}
