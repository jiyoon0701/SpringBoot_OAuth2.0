package com.example.authServerPassword.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenServices extends DefaultTokenServices implements AuthorizationServerTokenServices {
   private TokenStore tokenStore;
    private final TokenEnhancer tokenEnhancer;
  @Autowired@Qualifier("clientDetailsServiceImpl")
   private ClientDetailsService clientDetailsService;
    public CustomTokenServices(TokenStore tokenStore, TokenEnhancer tokenEnhancer, ClientDetailsService clientDetailsService) {
        this.tokenStore = tokenStore;
        this.tokenEnhancer = tokenEnhancer;
        this.clientDetailsService = clientDetailsService;
    }

        @Override
        public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
            String clientId = authentication.getOAuth2Request().getClientId();
            String tokenType = clientDetailsService.loadClientByClientId(clientId).getAdditionalInformation().get("token_type").toString();

            if (tokenType.equals("true")) {
                OAuth2AccessToken jwtAccessToken = generateJwtAccessToken(authentication);
               // tokenStore.storeAccessToken(jwtAccessToken, authentication);
                return jwtAccessToken;
            } else {
                OAuth2AccessToken accessToken = super.createAccessToken(authentication);
                tokenStore.storeAccessToken(accessToken, authentication);
                return accessToken;
            }
        }

        private OAuth2AccessToken generateJwtAccessToken(OAuth2Authentication authentication) {
            DefaultOAuth2AccessToken jwtAccessToken = new DefaultOAuth2AccessToken("generated-jwt-access-token");
            // jwtAccessToken.setExpiration(/* JWT 액세스 토큰의 만료 시간 설정 */);
          //  jwtAccessToken.setScope(/* JWT 액세스 토큰의 스코프 설정 */);
            // JWT 토큰에 필요한 추가 정보 설정
            jwtAccessToken = (DefaultOAuth2AccessToken) tokenEnhancer.enhance(jwtAccessToken, authentication);
            return jwtAccessToken;
        }

        // 필요한 다른 메서드 오버라이딩 및 구현

}

