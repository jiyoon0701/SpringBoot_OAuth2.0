package com.example.authServerPassword.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SpringConfigClass implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Spring MVC 프로젝트 설정을 위해 작성하는 클래스의 객체를 생성
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ServletAppContext.class);
      //  applicationContext.setConfigLocation("com.example.authServerPassword.config");

        // 요청 발생 시 요청을 처리하는 서블릿을 DispatcherServlet으로 설정해준다.
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        ServletRegistration.Dynamic  servlet = servletContext.addServlet("dispathcherServlet", dispatcherServlet);

        // 부가 설정
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        // Bean을 정의하는 클래스를 지정
        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
        rootAppContext.register(RootAppContext.class);

        // Listener
      //  servletContext.addListener(new ContextLoaderListener(applicationContext));


        // encodingFilter
    //    FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("encodingFilter", new CharacterEncodingFilter("UTF-8", true));
      //  filterRegistration.addMappingForUrlPatterns(null, true, "/*");

    }
}
