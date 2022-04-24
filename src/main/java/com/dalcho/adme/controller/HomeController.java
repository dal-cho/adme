package com.dalcho.adme.controller;

import com.dalcho.adme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {
   // index.html 과 연결
//    @GetMapping("/")
//    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        model.addAttribute("username", userDetails.getUsername());
//        return "index";
//    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails) { // 예시로 적어둠
        String user = userDetails.getUsername();
        return user;
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

//
//    @GetMapping("/registry") // 글 작성 페이지
//    public String upload(Model model) {
//        model.addAttribute("data","data");
//        return "registry";
//    }

}