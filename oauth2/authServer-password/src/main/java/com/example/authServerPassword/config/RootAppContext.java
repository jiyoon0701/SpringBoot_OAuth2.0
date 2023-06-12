package com.example.authServerPassword.config;


import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import({DataBaseConfig.class, MybatisConfig.class})
@ComponentScan({"com.example.authServerPassword.Service", "com.example.authServerPassword.Persistence"})
public class RootAppContext {

}
