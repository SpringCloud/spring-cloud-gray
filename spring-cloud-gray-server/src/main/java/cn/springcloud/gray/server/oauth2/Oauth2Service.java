package cn.springcloud.gray.server.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashMap;
import java.util.Map;

public class Oauth2Service {

    private static final Logger log = LoggerFactory.getLogger(Oauth2Service.class);

    private ClientDetailsService clientDetailsService;
    private OAuth2RequestFactory requestFactory;
    private DefaultTokenGranter defaultTokenGranter;
    private JwtTokenStore jwtTokenStore;

    public Oauth2Service(ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, DefaultTokenGranter defaultTokenGranter) {
        this.clientDetailsService = clientDetailsService;
        this.requestFactory = requestFactory;
        this.defaultTokenGranter = defaultTokenGranter;
    }

    public OAuth2AccessToken getAccessToken(TokenRequestInfo tokenRequestInfo) {
        return getAccessToken(tokenRequestInfo, defaultTokenGranter);
    }


    public OAuth2AccessToken getAccessToken(TokenRequestInfo tokenRequestInfo, TokenGranter tokenGranter) {
        String clientId = "gray-server";
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("grant_type", DefaultTokenGranter.GRANT_TYPE);

        Oauth2TokenRequest oauth2TokenRequest = Oauth2TokenRequest.builder()
                .clientId(clientId)
                .parameters(parameters)
                .tokenRequestInfo(tokenRequestInfo)
                .build();
        return createAccessToken(oauth2TokenRequest, tokenGranter);
    }

    private OAuth2AccessToken createAccessToken(Oauth2TokenRequest oauth2TokenRequest, TokenGranter tokenGranter) {
        ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(oauth2TokenRequest.getClientId());
        TokenRequest tokenRequest = requestFactory.createTokenRequest(oauth2TokenRequest.getParameters(), authenticatedClient);

        tokenRequest = new UserTokenRequest(tokenRequest,
                oauth2TokenRequest.getTokenRequestInfo().getUserDetails(),
                (Map) oauth2TokenRequest.getTokenRequestInfo().getExtensionProperties());
        return tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
    }


    public String getUserPrincipal(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userPrincipal = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userPrincipal = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userPrincipal = (String) authentication.getPrincipal();
            }
        }
        return userPrincipal;
    }



}
