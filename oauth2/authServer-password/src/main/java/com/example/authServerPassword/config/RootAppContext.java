package com.example.authServerPassword.config;


import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.example.authServerPassword.Service", "com.example.authServerPassword.Persistence"})
public class RootAppContext {

}
