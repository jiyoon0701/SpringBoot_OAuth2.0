package com.example.authServerPassword.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class mainController {

    @GetMapping("/")
    public String main() {
        return "index";
    }
}
