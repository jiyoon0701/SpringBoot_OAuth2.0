package com.example.authenticationserver.controller;

import com.example.authenticationserver.constrant.UserRole;
import com.example.authenticationserver.entity.ResourceOwner;
import com.example.authenticationserver.repository.ResourceOwnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/members")
@Slf4j
public class membersController {
    @Autowired
    private ResourceOwnerRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("register")
    public String registerPage(ResourceOwner user) {
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_USER);

        ResourceOwner owner = repository.findByUsername(user.getUsername());
        if(ObjectUtils.isEmpty(owner)){
            repository.save(user);
        }

        return "login";

    }

}
