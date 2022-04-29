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

    @GetMapping("/adme")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String user = userDetails.getUsername();
        return user;
    }

}