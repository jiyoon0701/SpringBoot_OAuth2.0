package com.example.authServerPassword.Controller;


import com.example.authServerPassword.Domain.ResourceOwner;
import com.example.authServerPassword.Service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/members")
@Slf4j
public class membersController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/save")
    public ResourceOwner save(ResourceOwner user) throws Exception {
        user.setId(1L);
        user.setUsername("test@test.com");
        user.setPassword(passwordEncoder.encode("test"));
        memberService.save(user);
        return user;
    }



}
