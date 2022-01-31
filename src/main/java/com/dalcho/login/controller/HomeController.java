package com.dalcho.login.controller;

import com.dalcho.login.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("data","data");
        return "home";
    }

    @GetMapping("/10s") // 10s 페이지
    public String ten(Model model) {
        model.addAttribute("data","data");
        return "tenseconds";
    }

//    @GetMapping("/space") // space 페이지
//    public String space(Model model) {
//        model.addAttribute("data","data");
//        return "space";
//    }

    @GetMapping("/board") // 글 작성 페이지
    public String board(Model model) {
        model.addAttribute("data","data");
        return "board";
    }

    @GetMapping("/registry") // 글 작성 페이지
    public String upload(Model model) {
        model.addAttribute("data","data");
        return "registry";
    }

}