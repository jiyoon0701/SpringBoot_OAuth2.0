package com.example.authServerPassword.config;


import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.example.authServerPassword.Service", "com.example.authServerPassword.Persistence", "com.example.authServerPassword.config"})
public class RootAppContext {
//    @Bean
//    public DataSource dataSource() {
//        // DataSource 설정
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/oauth2?allowPublicKeyRetrieval=true&amp;useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false&amp;serverTimezone=UTC");
//        dataSource.setUsername("root");
//        dataSource.setPassword("1234");
//        return dataSource;
//    }

//    @Bean
//    public MyBean myBean() {
//        return new MyBean();
//    }
//
//    @Import(OtherConfig.class)
//    public class OtherConfig {
//        // 다른 설정
//    }
}
