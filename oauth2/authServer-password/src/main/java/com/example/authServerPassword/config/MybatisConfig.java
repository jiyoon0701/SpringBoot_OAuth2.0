package com.example.authServerPassword.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MybatisConfig {
    @Bean
    @Autowired
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource2) throws Exception{
        SqlSessionFactoryBean sb = new SqlSessionFactoryBean();
        sb.setDataSource(dataSource2);
        sb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/*.xml"));
        //sb.setConfigLocation(new DefaultResourceLoader().getResource("classpath:config_mybatis.xml"));
        return sb.getObject();
    }

    @Bean
    @Autowired
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate s = new SqlSessionTemplate(sqlSessionFactory);
        return s;
    }

    @Bean
    @Autowired
    public SqlSessionFactory sqlSessionFactory2(DataSource dataSource2) throws Exception{
        SqlSessionFactoryBean sb = new SqlSessionFactoryBean();
        sb.setDataSource(dataSource2);
        sb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/*.xml"));
        //sb.setConfigLocation(new DefaultResourceLoader().getResource("classpath:config_mybatis.xml"));
        return sb.getObject();
    }

    @Bean
    @Autowired
    public SqlSessionTemplate sqlSession2(SqlSessionFactory sqlSessionFactory2){
        SqlSessionTemplate s = new SqlSessionTemplate(sqlSessionFactory2);
        return s;
    }
}
