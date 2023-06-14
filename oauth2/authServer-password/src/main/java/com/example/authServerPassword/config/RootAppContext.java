package com.example.authServerPassword.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.example.authServerPassword.Service", "com.example.authServerPassword.Persistence", "com.example.authServerPassword.utils"})
public class RootAppContext {

}
