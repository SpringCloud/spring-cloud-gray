package cn.springcloud.gray.oauth2;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

public class JWTOauth2Test {

    private static final Logger log = LoggerFactory.getLogger(JWTOauth2Test.class);

    @Test
    public void test(){
        JwtTokenStore jwtTokenStore = tokenStore();
        String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOnsiYSI6IkEiLCJiIjoiQiJ9LCJ1c2VyX25hbWUiOiJzYWxlc29uIiwic2NvcGUiOlsib3BlbmlkIl0sImV4cCI6MTUzNjkwOTU3NCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJmMWZmM2UxZS1mYWYxLTRiNmEtODU0Yi1lMWUxZTg1NWIyMDgiLCJjbGllbnRfaWQiOiJqaWFkYW8ifQ.htL5nJu3ZuLhYqTh936-W_Kj4J4FGzjR064TP3YvTSqUkRdlNajJdm6h1wtSEykuvREOt-i5I72EhmmfTV0xjkcgBVrRXP6YcUNTIhEjuEy6upnd8yJhldFtUQwlUOIsXiMfdXa9nI-8ZeGr13JJESOOuvij3dRPILlxpW9mcE52hNSwCfcAh7yNA4KmuBDfsAgyaTe_qZcmnRPzQl_6P3ZrYgK8iTzKY-1LjGeqZ3vGAygP0-ppvQHAHyGp1MTePj3STywz9N05WStLA_Tav1NQksezAneTp4Im33pbuAWnz0e8FslQGYQ9YOfZgrwY9DEh_ARV2aFfpnwx_HRFeQ";
        log.info("accessToken: {}", accessToken);
        OAuth2AccessToken oAuth2AccessToken = jwtTokenStore.readAccessToken(accessToken);
        log.info("oAuth2AccessToken.additionalInformation: {}", oAuth2AccessToken.getAdditionalInformation());
    }


    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("keystore.jks"), "password".toCharArray())
                .getKeyPair("selfsigned");
        converter.setKeyPair(keyPair);
        return converter;
    }
}
