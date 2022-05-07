package com.dalcho.adme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {

//    @GetMapping("/adme")
//    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
//        String user = userDetails.getUser().getNickname();
//        return user;
//    }

//    @GetMapping("/adme")
//    public Member home(@ModelAttribute MemberDto memberDto) throws Exception {
//        return memberService.setUsername(memberDto);
//    }

}