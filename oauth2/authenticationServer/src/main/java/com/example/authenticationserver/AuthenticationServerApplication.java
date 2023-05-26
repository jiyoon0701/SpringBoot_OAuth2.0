package com.example.authenticationserver;

import com.example.authenticationserver.constrant.UserRole;
import com.example.authenticationserver.entity.ResourceOwner;
import com.example.authenticationserver.repository.ResourceOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthenticationServerApplication implements CommandLineRunner {
    @Autowired
    private ResourceOwnerRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ResourceOwner user = new ResourceOwner();
        user.setUsername("test");
        user.setUseremail("test@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(UserRole.ROLE_USER);

        repository.save(user);
    }
}
