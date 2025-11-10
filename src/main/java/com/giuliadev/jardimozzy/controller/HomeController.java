package com.giuliadev.jardimozzy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/historia")
    public String historia() {
        return "historia";
    }

    @GetMapping("/jardim")
    public String jardim() {
        return "jardim";
    }

}