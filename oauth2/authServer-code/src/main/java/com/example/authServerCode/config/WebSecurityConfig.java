package com.example.authServerCode.config;

import com.example.authServerCode.security.CustomAuthenticationFailureHandler;
import com.example.authServerCode.security.CustomAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.PrintWriter;


@EnableWebSecurity // 스프링시큐리티 사용을 위한 어노테이션
@Configuration
@AllArgsConstructor
/**
 * WebSecurityConfigurerAdapter가 Deprecated가 되어 사용할 수 없게 됨.
 * 공식문서에 따라 SecurityFilterChain을 Bean으로 등록해서 사용
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * AuthenticationProvider 인터페이스는 화면에서 입력한 로그인 정보랑 DB에서 가져온 사용자 정보를 비교해주는 인터페이스
     * 해당 인터페이스에 오버라이드되는 authentication()는 화면에서 입력한 로그인 정보를 담고 있는 Authentication 객체를 갖고있다.
     * 그리고 DB에서 사용자의 정보를 가져오는 건 UserDetailService 인터페이스에서 loadUserByUsername()으로 구현
     * 따라서 authentication()에서 loadUserByUsername()을 이용해 DB에서 사용자 정보를 가져와서 Authentication객체(화면에서 가져온 정보)를 비교한다.
     */

    /**
     * AuthenticationManager 빈 생성 시 스프링의 내부 동작으로 인해 UserSecurityService와 PasswordEncoder가 자동으로 설정된다.
     */
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .antMatchers("classpath:/resource/static/**")
                .antMatchers("/vendor/**")
                .antMatchers("/js/**")
                .antMatchers("/favicon*/**")
                .antMatchers("/img/**")
                .antMatchers("classpath:/static/css/")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/members/login", "/api/members/register").permitAll()
                .antMatchers("/error**").permitAll()
                .anyRequest().authenticated()
                .and().csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler((request,response,exception)->{
                    response.setContentType("application/json;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-cache");
                    PrintWriter writer = response.getWriter();
                    writer.println(new AccessDeniedException("access denied !"));
                })
                .and()
                .formLogin()
                .loginPage("/login.html")
                .usernameParameter("username")//아이디 파라미터명 설정
                .passwordParameter("password")//패스워드 파라미터명 설정
                .loginProcessingUrl("/login")//로그인 Form Action Url
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
        ;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/api/client/dashboard");

        return successHandler;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        CustomAuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler();
        failureHandler.setDefaultFailureUrl("/api/members/login?error=loginfail");

        return failureHandler;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/api/members/login");
    }

}

