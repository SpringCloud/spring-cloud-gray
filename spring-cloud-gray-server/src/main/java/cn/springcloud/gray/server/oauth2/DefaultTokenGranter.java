package cn.springcloud.gray.server.oauth2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;

/**
 * Created by admin on 2016/12/30.
 */
public class DefaultTokenGranter extends AbstractTokenGranter {
    public static final String GRANT_TYPE = "default";

    public DefaultTokenGranter(
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        UserDetails userDetails = null;
        if(tokenRequest instanceof UserTokenRequest){
            userDetails = ((UserTokenRequest)tokenRequest).getUserDetails();
        }else {
//            tokenRequest.getRequestParameters().get("");
//            String phoneNumber = tokenRequest.getRequestParameters().get("phoneNumber");
//            userDetails = userDetailsService.loadUserByPhoneNumber(phoneNumber);
        }
        return getOAuth2Authentication(client, tokenRequest, userDetails);
    }


    public OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest, UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
//        Authentication authentication = new OAuth2Authentication();
        OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, authentication);
    }


}
