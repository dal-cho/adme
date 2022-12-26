package com.dalcho.adme.controller;

import com.dalcho.adme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    // userService 생성자 생성
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 로그인 페이지

    @GetMapping("/user/login/error")
    public Model loginError(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false)
                                    String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return model;
    }


}
