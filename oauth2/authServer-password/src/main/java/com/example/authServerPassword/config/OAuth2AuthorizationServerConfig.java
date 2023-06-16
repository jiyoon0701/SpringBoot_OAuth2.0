package com.example.authServerPassword.config;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import javax.sql.DataSource;
import java.io.PrintWriter;

@Configuration
//@Log4j
@EnableAuthorizationServer
@Slf4j
/**
 * @EnableAuthorizationServer
 * Authorization을 발급하는 서버로 지정되며, 해당 어노테이션을 붙이는 것만으로도 OAuth관련 endPoints가 생성된다.
 * (/oauth/token, /oauth/authorize 등)
 */
@PropertySource(value = "classpath:jwt.properties")
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * 모든 메서드를 포함한 구현체 -> AuthorizationServerConfigurerAdapter이기 때문에 상속을 받으면 편리하다.
     */

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired private DataSource dataSource ;
    @Autowired@Qualifier("clientDetailsServiceImpl") ClientDetailsService clientDetailsService;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * AuthorizationServerSecurityConfigurer를 매개변수로 가진 설정 코드
     * 해당 메소드에서 설정하는 것은 AuthenticationEntryPoint, AccessDeniedHandler, PasswordEncoder등을 설정할 수 있는 메소드
     * Spring Security에 적용된 AuthenticationEntryPoint, AccessDeniedHandler, PasswordEncoder가 사용된다.
     *
     * 토큰 엔드포인트 (/auth/token) 에 대한 보안관련 설정을 할 수 있다.
     * */

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.accessDeniedHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-cache");
                    PrintWriter writer = response.getWriter();
                    writer.println(new AccessDeniedException("access denied !"));
                })
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-cache");
                    PrintWriter writer = response.getWriter();
                    writer.println(new AccessDeniedException("access denied !"));
                })
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder)
                .allowFormAuthenticationForClients();
    }

    /**
     클라이언트에 대한 인증 처리를 위한 설정 - JDBC -> JdbcClientDetailsService
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
         * 클라이언트를 db에서 관리하기 위하여 DataSource 주입
         * UserDetailsService와 동일한 역할을 하는 객체
         * userDetailsService는 spring Security에서 유저의 정보를 가져오는 인터페이스이다.
         *
         * */
        clients.withClientDetails(clientDetailsService);
        clients.configure(new ClientDetailsServiceBuilder<>());
    }

    /**
     * 이 설정은 Authorization Server 설정의 전부라고 해도 무방할 정도로 중요한 설정 메소드이다.
     * Authorize, Token 발급, Token Check할 때의 행동을 정의하는 설정들이 들어간다.
     * Authorization Endpoint: /oauth/authorize
     * Token Endpoint: /oauth/token
     * Approval Endpoint: /oauth/confirm_access
     * Check Token Endpoint: /oauth/check_token
     * JWT Sign key Endpoint: /oauth/token_key
     * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {


        log.info("Configure token_type Check :::::::::{}", getClientId());
        // 클라이언트 인증 필터를 추가합니다.
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //refresh token 발급을 위해서는 UserDetailsService(AuthenticationManager authenticate()에서 사용)필요
                //  .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource)) 코드 발급
                // 1. authorization code를 DB로 관리 코드 테이블의 authentication은 blob데이터타입으로..
                // 2. client가 얻는 인증코드를 다루는 service 클래스를 등록하는 설정이다.
                .approvalStore(approvalStore()) //리소스 소유자의 승인을 추가, 검색, 취소하기 위한 메소드를 정의
                // 1. resource owner가 client app이 resource server에 있는 resource owner의 리소스의 사용을 허락한다는 데이터를 담은 approvalStore를 설정해주는 것
                //.tokenStore(tokenStore()) //토큰과 관련된 인증 데이터를 저장, 검색, 제거, 읽기를 정의 - jwt
                .tokenStore(tokenStore(dataSource)); //토큰과 관련된 인증 데이터를 저장, 검색, 제거, 읽기를 정의 - accessToken
                //.accessTokenConverter(accessTokenConverter());
    }

    /**
     * jwt 발급 시 필요코드
     * @return
     */
    /*@Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }*/

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSecret);

        return converter;
    }

    // token type 반환

    public String getClientId() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication
       ();
        if (!(a instanceof OAuth2Authentication)) {
            return null;
        }
        return ((OAuth2Authentication) a).getOAuth2Request().getClientId();
    }


    /**
     *  accessToken 발급 시 필요 코드
     */
    @Bean
    public TokenStore tokenStore(DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public JdbcApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 새로운 클라이언트 등록을 위한 빈
     */
    @Bean
    public ClientRegistrationService clientRegistrationService() {
        return new JdbcClientDetailsService(dataSource);
    }
}
