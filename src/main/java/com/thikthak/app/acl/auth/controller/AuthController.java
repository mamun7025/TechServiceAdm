package com.thikthak.app.acl.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {
    @GetMapping("login")
    public String getLoginView(){
        return "/auth/login";
    }

    @GetMapping("403")
    public String error403(){
        return  "/auth/403";
    }
}
