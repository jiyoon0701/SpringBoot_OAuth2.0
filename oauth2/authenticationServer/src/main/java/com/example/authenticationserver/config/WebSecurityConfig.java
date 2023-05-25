package com.example.authenticationserver.config;

import com.example.authenticationserver.security.CustomAuthenticationFailureHandler;
import com.example.authenticationserver.security.CustomAuthenticationSuccessHandler;
import com.example.authenticationserver.security.ResourceOwnerAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;


@EnableWebSecurity // 스프링시큐리티 사용을 위한 어노테이션
@Configuration
/**
 * WebSecurityConfigurerAdapter가 Deprecated가 되어 사용할 수 없게 됨.
 * 공식문서에 따라 SecurityFilterChain을 Bean으로 등록해서 사용
 */
public class WebSecurityConfig {

    /**
     * AuthenticationProvider 인터페이스는 화면에서 입력한 로그인 정보랑 DB에서 가져온 사용자 정보를 비교해주는 인터페이스
     * 해당 인터페이스에 오버라이드되는 authentication()는 화면에서 입력한 로그인 정보를 담고 있는 Authentication 객체를 갖고있다.
     * 그리고 DB에서 사용자의 정보를 가져오는 건 UserDetailService 인터페이스에서 loadUserByUsername()으로 구현
     * 따라서 authentication()에서 loadUserByUsername()을 이용해 DB에서 사용자 정보를 가져와서 Authentication객체(화면에서 가져온 정보)를 비교한다.
     */

    /**
     * AuthenticationManager 빈 생성 시 스프링의 내부 동작으로 인해 위에서 작성한 UserSecurityService와 PasswordEncoder가 자동으로 설정된다.
     */
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/css/**", "/vendor/**","/js/**", "/favicon*/**", "/img/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests((authz) -> authz  // 요청에 의한 보안검사 시작
                        .antMatchers("/api/members/login", "/api/members/register").permitAll()
                        .antMatchers("/error**").permitAll()
                        .anyRequest().authenticated())  // 어떤 요청에도 보안검사를 한다.
               //.addFilter(authenticationFilter()) // 필터 추가 (loginForm)
                .exceptionHandling() // 인증, 인가에서 Error 발생시 후처리
                .authenticationEntryPoint(authenticationEntryPoint()) // 인증이 되지 않은 유저가 요청 시 동작
                .accessDeniedHandler(((request, response, accessDeniedException) -> { // 인가 예외 처리 (권한을 체크 후 액세스 할 수 없는 요청 시 동작)
                    response.setContentType("application/json;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-cache");
                    PrintWriter writer = response.getWriter();
                    writer.println(new AccessDeniedException("Access Denied!"));
                    writer.flush();
                    writer.close();
                }))
                .and()
                .formLogin()
                .loginPage("/login.html")
                .defaultSuccessUrl("/home")
                .failureUrl("/login.html?error=true")// 로그인 실패 후 이동 페이지
                .usernameParameter("username")//아이디 파라미터명 설정
                .passwordParameter("password")//패스워드 파라미터명 설정
                .loginProcessingUrl("/login")//로그인 Form Action Url
                .successHandler(authenticationSuccessHandler())//로그인 성공 후 핸들러 (해당 핸들러를 생성하여 핸들링 해준다.)
                .failureHandler(authenticationFailureHandler());//로그인 실패 후 핸들러 (해당 핸들러를 생성하여 핸들링 해준다.)

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/index");

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

