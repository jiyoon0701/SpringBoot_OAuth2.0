package com.example.authServerPassword.Controller;


import com.example.authServerPassword.Domain.ResourceOwner;
import com.example.authServerPassword.Service.MemberService;
import com.example.authServerPassword.constrant.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@Slf4j
public class membersController {
    @Autowired
    private MemberService memberService;

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

    @PostMapping("/save")
    public String save(ResourceOwner user) throws Exception {
        user.setUsername("test@test.com");
        user.setPassword(passwordEncoder.encode("test"));
        user.setRole(UserRole.ROLE_USER);
        ResourceOwner owner = memberService.findByUsername(user.getUsername());
        if(ObjectUtils.isEmpty(owner)){
            memberService.save(user);
        }
        return "login";
    }

    @PostMapping("/register")
    public String register(ResourceOwner user) throws Exception {
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        ResourceOwner owner = memberService.findByUsername(user.getUsername());
        if(ObjectUtils.isEmpty(owner)){
            memberService.save(user);
        }
        return "login";
    }
}
