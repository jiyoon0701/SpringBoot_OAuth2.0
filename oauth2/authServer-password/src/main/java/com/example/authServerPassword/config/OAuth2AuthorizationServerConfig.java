package com.example.authServerPassword.config;

// import com.example.authServerPassword.Service.ClientDetailsServiceImpl;
import com.example.authServerPassword.Service.ClientDetailsServiceImpl;
import com.example.authServerPassword.utils.ShaPasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import javax.sql.DataSource;
import java.io.PrintWriter;


@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
@Log4j
/**
 * Authorization을 발급하는 서버로 지정되며, 해당 어노테이션을 붙이는 것만으로도 OAuth관련 endPoints가 생성된다.
 * (/oauth/token, /oauth/authorize 등)
 */
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * 모든 메서드를 포함한 구현체 -> AuthorizationServerConfigurerAdapter이기 때문에 상속을 받으면 편리하다.
     */

    @Autowired private DataSource dataSource ;
   // @Autowired@Qualifier("clientDetailsServiceImpl") ClientDetailsService clientDetailsService;
    @Autowired private ClientDetailsServiceImpl clientDetailsService;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private AuthenticationManager authenticationManager;

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
                .addTokenEndpointAuthenticationFilter(clientCredentialsTokenEndpointFilter());
    }

    @Bean
    public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter() {
        ClientCredentialsTokenEndpointFilter filter = new ClientCredentialsTokenEndpointFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }


//    @Bean
//    public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter() {
//
//        ClientCredentialsTokenEndpointFilter filter = new ClientCredentialsTokenEndpointFilter();
//
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }


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
        //clients.jdbc().passwordEncoder()
        System.out.println("-------------------");

       // clients.withClientDetails(clientDetailsService);
//        clients.jdbc(dataSource);
//        clients.withClientDetails(clientDetailsService);
//        System.out.println(clientDetailsService.listClientDetails());

        clients
                .inMemory()
                .withClient("bb39b83c-6065-4277-8014-c4eac41f14e2")
                .secret("e1d69da20a8d342d52e8fa08a1a67ed5d916a6d20df15b8e6f17aaaaf9afbe2e")  // password
                .redirectUris("http://localhost:9000/callback")
                .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials") // client_credentials 추가
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(240);
               //.scopes("read_profile");
//                .inMemory() // (1)
//                .withClient("bb39b83c-6065-4277-8014-c4eac41f14e2") //(2)
//                .secret("e1d69da20a8d342d52e8fa08a1a67ed5d916a6d20df15b8e6f17aaaaf9afbe2e")  //(3) password 9156f2c0-e1f5-4f65-9905-cd6dfe9d0af4
//                .redirectUris("http://localhost:9000/callback") // (4)
//                .authorizedGrantTypes("client_credentials") // (5)
//                .scopes("read_profile"); // (6)
    }

        //clients.jdbc(dataSource).passwordEncoder(passwordEncoder);


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
        // 클라이언트 인증 필터를 추가합니다.
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //refresh token 발급을 위해서는 UserDetailsService(AuthenticationManager authenticate()에서 사용)필요
             //  .tokenServices(tokenServices())
                .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource))

                // 1. authorization code를 DB로 관리 코드 테이블의 authentication은 blob데이터타입으로..
                // 2. client가 얻는 인증코드를 다루는 service 클래스를 등록하는 설정이다.
                .approvalStore(approvalStore()) //리소스 소유자의 승인을 추가, 검색, 취소하기 위한 메소드를 정의
                // 1. resource owner가 client app이 resource server에 있는 resource owner의 리소스의 사용을 허락한다는 데이터를 담은 approvalStore를 설정해주는 것
                .tokenStore(tokenStore()) //토큰과 관련된 인증 데이터를 저장, 검색, 제거, 읽기를 정의
                .accessTokenConverter(accessTokenConverter());
    }
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JdbcApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("non-prod-signature");

        return converter;
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setTokenEnhancer(accessTokenConverter());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetailsService());
        return tokenServices;
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        // 클라이언트 정보를 데이터베이스에서 관리하는 구현체 설정
        return new JdbcClientDetailsService(dataSource);
    }

//    @Bean
//    public ClientRegistrationService clientRegistrationService() {
//        // 클라이언트 등록을 처리하는 구현체 설정
//        return new ClientDetailsServiceImpl(dataSource);
//    }

    /**
     * 새로운 클라이언트 등록을 위한 빈
     */
    @Bean
    public ClientRegistrationService clientRegistrationService() {
        return new JdbcClientDetailsService(dataSource);
    }
}
