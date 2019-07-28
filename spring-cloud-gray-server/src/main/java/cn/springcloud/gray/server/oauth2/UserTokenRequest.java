package cn.springcloud.gray.server.oauth2;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserTokenRequest extends TokenRequest {


    private static final long serialVersionUID = 7584363578550394391L;



    private UserDetails userDetails;


    private Map<String, Serializable> extensionProperties;

    public UserTokenRequest(Map<String, String> requestParameters, String clientId, Collection<String> scope,
                            String grantType) {
        super(requestParameters, clientId, scope, grantType);
    }

    public UserTokenRequest(TokenRequest tokenRequest, UserDetails userDetails, Map<String, Serializable> extensionProperties){
        this(tokenRequest.getRequestParameters(), tokenRequest.getClientId(), tokenRequest.getScope(), tokenRequest.getGrantType());
        setExtensionProperties(extensionProperties);
        this.userDetails = userDetails;
    }


    public Map<String, Serializable> getExtensionProperties() {
        return extensionProperties;
    }

    public void setExtensionProperties(Map<String, Serializable> extensionProperties) {
        this.extensionProperties = extensionProperties;
    }

    public UserDetails getUserDetails(){
        return userDetails;
    }

    public OAuth2Request createOAuth2Request(ClientDetails client) {
        Map<String, String> requestParameters = getRequestParameters();
        HashMap<String, String> modifiable = new HashMap<String, String>(requestParameters);
        // Remove password if present to prevent leaks
        modifiable.remove("password");
        modifiable.remove("client_secret");
        // Add grant type so it can be retrieved from OAuth2Request
        modifiable.put("grant_type", getGrantType());
        return new OAuth2Request(modifiable, client.getClientId(), client.getAuthorities(), true, this.getScope(),
            client.getResourceIds(), null, null, extensionProperties);
    }

}
